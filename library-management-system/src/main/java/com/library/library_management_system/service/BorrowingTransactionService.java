package com.library.library_management_system.service;

import com.library.library_management_system.client.CmsClient;
import com.library.library_management_system.client.EmailClient;
import com.library.library_management_system.dto.*;
import com.library.library_management_system.dto.converter.BorrowingTransactionConverter;
import com.library.library_management_system.exception.*;
import com.library.library_management_system.model.*;
import com.library.library_management_system.repository.BorrowingTransactionRepository;
import com.library.library_management_system.utils.CommonEnum.*;
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
    private final EmailClient emailClient;
    private final BorrowingTransactionConverter converter;

    @Value("${borrowing.max-period-days:30}")
    private int maxBorrowPeriodDays;

    public List<BorrowingTransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(converter::toDto)
                .map(this::enrich)
                .toList();
    }

    public BorrowingTransactionResponseDTO getTransactionById(UUID id) {
        return enrich(converter.toDto(getTransactionEntity(id)));
    }

    public BorrowingTransactionResponseDTO borrowBook(UUID bookId, UUID borrowerId, int durationDays) {
        validateBorrowPeriod(durationDays);
        validateEligibility(bookId, borrowerId);

        Book book = bookService.getBookEntity(bookId);
        Borrower borrower = borrowerService.getBorrowerEntity(borrowerId);
        BigDecimal totalAmount = book.calculateTotalPrice(durationDays);

        TransactionResponseDTO payment = processPayment(borrower.getCardNumberHash(), totalAmount, TransactionType.DEBIT);

        BorrowingTransaction transaction = createTransaction(bookId, borrowerId, durationDays, totalAmount, payment.getId());
        bookService.markBookAsBorrowed(bookId);
        borrowerService.addTransactionToBorrower(borrowerId, transaction.getId());

        sendEmailNotification(borrower.getEmail(), book.getTitle(), totalAmount);

        return enrich(converter.toDto(transactionRepository.save(transaction)));
    }

    public BorrowingTransactionResponseDTO returnBook(UUID transactionId) {
        BorrowingTransaction transaction = getBorrowedTransaction(transactionId);
        transaction.setReturnDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.RETURNED);

        bookService.markBookAsAvailable(transaction.getBookId());
        borrowerService.removeTransactionFromBorrower(transaction.getBorrowerId(), transactionId);

        if (isReturnedOnTime(transaction)) {
            refundInsurance(transaction);
        }

        return enrich(converter.toDto(transactionRepository.save(transaction)));
    }

    private BorrowingTransaction createTransaction(UUID bookId, UUID borrowerId, int days, BigDecimal amount, UUID paymentId) {
        BorrowingTransaction tx = new BorrowingTransaction();
        tx.setBookId(bookId);
        tx.setBorrowerId(borrowerId);
        tx.setBorrowDate(LocalDateTime.now());
        tx.setDueDate(LocalDateTime.now().plusDays(days));
        tx.setTransactionAmount(amount);
        tx.setStatus(TransactionStatus.BORROWED);
        tx.setCmsTransactionId(paymentId);
        return tx;
    }

    private BorrowingTransactionResponseDTO enrich(BorrowingTransactionResponseDTO dto) {
        dto.setBookTitle(bookService.getBookTitle(dto.getBookId()));
        dto.setBorrowerName(borrowerService.getBorrowerName(dto.getBorrowerId()));
        return dto;
    }

    private TransactionResponseDTO processPayment(String cardHash, BigDecimal amount, TransactionType type) {
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setCardNumberHash(cardHash);
        request.setTransactionAmount(amount);
        request.setTransactionType(type);

        TransactionResponseDTO response = cmsClient.processTransaction(request);
        if (response.getStatus() != Status.SUCCESS) {
            throw new PaymentProcessingException(response.getMessage());
        }
        return response;
    }

    private void refundInsurance(BorrowingTransaction transaction) {
        Book book = bookService.getBookEntity(transaction.getBookId());
        Borrower borrower = borrowerService.getBorrowerEntity(transaction.getBorrowerId());

        TransactionResponseDTO refund = processPayment(
                borrower.getCardNumberHash(),
                book.getInsuranceFees(),
                TransactionType.CREDIT
        );

        transaction.setInsuranceRefunded(refund.getStatus() == Status.SUCCESS);
    }

    private void sendEmailNotification(String toEmail, String bookTitle, BigDecimal price) {
        EmailRequest email = new EmailRequest();
        email.setEmail(toEmail);
        email.setMessage("You have successfully borrowed the book: " + bookTitle +
                ". Total price: " + price + " USD.");
        emailClient.sendEmail(email);
    }

    private void validateBorrowPeriod(int days) {
        if (days < 1 || days > maxBorrowPeriodDays) {
            throw new BusinessRuleException("Borrow period must be between 1 and %d days".formatted(maxBorrowPeriodDays));
        }
    }

    private void validateEligibility(UUID bookId, UUID borrowerId) {
        bookService.validateBookAvailable(bookId);
        borrowerService.validateBorrowerActive(borrowerId);
    }

    private BorrowingTransaction getTransactionEntity(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
    }

    private BorrowingTransaction getBorrowedTransaction(UUID id) {
        BorrowingTransaction tx = getTransactionEntity(id);
        if (tx.getStatus() != TransactionStatus.BORROWED) {
            throw new BusinessRuleException("Transaction is not in BORROWED state");
        }
        return tx;
    }

    private boolean isReturnedOnTime(BorrowingTransaction tx) {
        return tx.getReturnDate().isBefore(tx.getDueDate());
    }
}
