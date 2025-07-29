package com.library.library_management_system.service;

import com.library.library_management_system.client.CmsClient;
import com.library.library_management_system.dto.*;
import com.library.library_management_system.dto.converter.BorrowingTransactionConverter;
import com.library.library_management_system.exception.*;
import com.library.library_management_system.model.*;
import com.library.library_management_system.repository.BorrowingTransactionRepository;
import com.library.library_management_system.utils.CommonEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowingTransactionService {
    private final BorrowingTransactionRepository transactionRepository;
    private final BookService bookService;
    private final BorrowerService borrowerService;
    private final CmsClient cmsClient;
    private final BorrowingTransactionConverter transactionConverter;

    @Value("${borrowing.max-period-days:30}")
    private int maxBorrowPeriodDays;

    public List<BorrowingTransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionConverter::toDto)
                .map(this::enrichTransactionDTO)
                .toList();
    }

    public BorrowingTransactionResponseDTO getTransactionById(UUID id) {
        return enrichTransactionDTO(
                transactionConverter.toDto(getTransactionEntity(id))
        );
    }

    @Transactional
    public BorrowingTransactionResponseDTO borrowBook(UUID bookId, UUID borrowerId, int durationDays) {
        validateBorrowPeriod(durationDays);
        validateTransactionEligibility(bookId, borrowerId);

        Book book = bookService.getBookEntity(bookId);
        Borrower borrower = borrowerService.getBorrowerEntity(borrowerId);
        BigDecimal amount = book.calculateTotalPrice(durationDays);

        TransactionResponseDTO paymentResponse = processPayment(borrower, amount);
        BorrowingTransaction transaction = createTransaction(
                bookId, borrowerId, durationDays, amount, paymentResponse.getId()
        );

        updateSystemState(bookId, borrowerId, transaction.getId());
        return enrichTransactionDTO(
                transactionConverter.toDto(transactionRepository.save(transaction))
        );
    }

    @Transactional
    public BorrowingTransactionResponseDTO returnBook(UUID transactionId) {
        BorrowingTransaction transaction = getBorrowedTransaction(transactionId);
        transaction.setReturnDate(LocalDateTime.now());
        transaction.setStatus(CommonEnum.TransactionStatus.RETURNED);

        bookService.markBookAsAvailable(transaction.getBookId());
        borrowerService.removeTransactionFromBorrower(transaction.getBorrowerId(), transactionId);

        if (isReturnedOnTime(transaction)) {
            processInsuranceRefund(transaction);
        }

        return enrichTransactionDTO(
                transactionConverter.toDto(transactionRepository.save(transaction))
        );
    }

    private BorrowingTransaction createTransaction(UUID bookId, UUID borrowerId,
                                                   int durationDays, BigDecimal amount,
                                                   UUID paymentId) {
        BorrowingTransaction transaction = new BorrowingTransaction();
        transaction.setBookId(bookId);
        transaction.setBorrowerId(borrowerId);
        transaction.setBorrowDate(LocalDateTime.now());
        transaction.setDueDate(LocalDateTime.now().plusDays(durationDays));
        transaction.setStatus(CommonEnum.TransactionStatus.BORROWED);
        transaction.setTransactionAmount(amount);
        transaction.setCmsTransactionId(paymentId);
        return transaction;
    }

    private void updateSystemState(UUID bookId, UUID borrowerId, UUID transactionId) {
        bookService.markBookAsBorrowed(bookId);
        borrowerService.addTransactionToBorrower(borrowerId, transactionId);
    }

    private TransactionResponseDTO processPayment(Borrower borrower, BigDecimal amount) {
        TransactionRequestDTO paymentRequest = new TransactionRequestDTO();
        paymentRequest.setTransactionAmount(amount);
        paymentRequest.setTransactionType(CommonEnum.TransactionType.DEBIT);
        paymentRequest.setCardNumberHash(borrower.getCardNumberHash());

        TransactionResponseDTO response = cmsClient.processTransaction(paymentRequest);
        if (response.getStatus() != CommonEnum.Status.SUCCESS) {
            throw new PaymentProcessingException(response.getMessage());
        }
        return response;
    }

    private void processInsuranceRefund(BorrowingTransaction transaction) {
        Book book = bookService.getBookEntity(transaction.getBookId());
        Borrower borrower = borrowerService.getBorrowerEntity(transaction.getBorrowerId());

        TransactionRequestDTO refundRequest = new TransactionRequestDTO();
        refundRequest.setTransactionAmount(book.getInsuranceFees());
        refundRequest.setTransactionType(CommonEnum.TransactionType.CREDIT);
        refundRequest.setCardNumberHash(borrower.getCardNumberHash());

        TransactionResponseDTO response = cmsClient.processTransaction(refundRequest);
        transaction.setInsuranceRefunded(response.getStatus() == CommonEnum.Status.SUCCESS);
    }

    private BorrowingTransactionResponseDTO enrichTransactionDTO(BorrowingTransactionResponseDTO dto) {
        dto.setBookTitle(bookService.getBookTitle(dto.getBookId()));
        dto.setBorrowerName(borrowerService.getBorrowerName(dto.getBorrowerId()));
        return dto;
    }

    private void validateBorrowPeriod(int durationDays) {
        if (durationDays < 1 || durationDays > maxBorrowPeriodDays) {
            throw new BusinessRuleException(
                    "Borrow period must be between 1 and %d days".formatted(maxBorrowPeriodDays)
            );
        }
    }

    private void validateTransactionEligibility(UUID bookId, UUID borrowerId) {
        bookService.validateBookAvailable(bookId);
        borrowerService.validateBorrowerActive(borrowerId);
    }

    private BorrowingTransaction getTransactionEntity(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
    }

    private BorrowingTransaction getBorrowedTransaction(UUID id) {
        BorrowingTransaction transaction = getTransactionEntity(id);
        if (transaction.getStatus() != CommonEnum.TransactionStatus.BORROWED) {
            throw new BusinessRuleException("Transaction is not in BORROWED state");
        }
        return transaction;
    }

    private boolean isReturnedOnTime(BorrowingTransaction transaction) {
        return transaction.getReturnDate().isBefore(transaction.getDueDate());
    }
}