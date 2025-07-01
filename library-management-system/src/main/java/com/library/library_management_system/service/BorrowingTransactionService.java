package com.library.library_management_system.service;


import com.library.library_management_system.client.CmsClient;
import com.library.library_management_system.client.EmailClient;
import com.library.library_management_system.dto.*;
import com.library.library_management_system.emun.Status;
import com.library.library_management_system.emun.TransactionStatus;
import com.library.library_management_system.emun.TransactionType;
import com.library.library_management_system.exception.*;
import com.library.library_management_system.model.*;
import com.library.library_management_system.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowingTransactionService {

    private static final Logger log = LoggerFactory.getLogger(BorrowingTransactionService.class);

    private final BorrowingTransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final CmsClient cmsClient;
    private final EmailClient emailClient;

    @Value("${borrowing.max-period-days:30}")
    private int maxBorrowPeriodDays;

    public BorrowingTransactionService(BorrowingTransactionRepository transactionRepository,
                                       BookRepository bookRepository,
                                       BorrowerRepository borrowerRepository,
                                       CmsClient cmsClient,
                                       EmailClient emailClient) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
        this.cmsClient = cmsClient;
        this.emailClient = emailClient;
    }

    public BorrowingTransactionResponseDTO borrowBook(Long bookId, Long borrowerId, int durationDays) {
        // Validate duration
        if (durationDays < 1 || durationDays > maxBorrowPeriodDays) {
            throw new BusinessRuleException(
                    String.format("Borrow period must be between 1 and %d days", maxBorrowPeriodDays)
            );
        }

        // Retrieve entities
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));

        // Validate business rules
        if (!book.isAvailableForBorrow()) {
            throw new BusinessRuleException("Book is not available for borrowing");
        }
        if (!borrower.canBorrow()) {
            throw new BusinessRuleException("Borrower is not eligible to borrow books");
        }

        // Calculate amount
        BigDecimal amount = book.calculateTotalPrice(durationDays);
        LocalDateTime dueDate = LocalDateTime.now().plusDays(durationDays);

        // Process payment via CMS
        TransactionRequestDTO paymentRequest = new TransactionRequestDTO();
        paymentRequest.setTransactionAmount(amount);
        paymentRequest.setTransactionDate(LocalDateTime.now());
        paymentRequest.setTransactionType(TransactionType.DEBIT);
        paymentRequest.setCardNumberHash(borrower.getCardNumberHash());

        TransactionResponseDTO paymentResponse = cmsClient.processTransaction(paymentRequest);

        if (paymentResponse.getStatus() != Status.SUCCESS) {
            throw new PaymentProcessingException(paymentResponse.getMessage());
        }

        // Create transaction record
        BorrowingTransaction transaction = new BorrowingTransaction();
        transaction.setBook(book);
        transaction.setBorrower(borrower);
        transaction.setBorrowDate(LocalDateTime.now());
        transaction.setDueDate(dueDate);
        transaction.setStatus(TransactionStatus.BORROWED);
        transaction.setTransactionAmount(amount);
        transaction.setCmsTransactionId(paymentResponse.getId());

        // Update book status
        book.setAvailable(false);
        bookRepository.save(book);

        // Save transaction
        BorrowingTransaction savedTransaction = transactionRepository.save(transaction);

        // Send confirmation email
        sendBorrowConfirmation(borrower, book, amount);

        log.info("Book borrowed: {}", savedTransaction);
        return BorrowingTransactionResponseDTO.fromEntity(savedTransaction);
    }

    public BorrowingTransactionResponseDTO returnBook(Long transactionId) {
        BorrowingTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.BORROWED) {
            throw new BusinessRuleException("Book is not currently borrowed");
        }

        // Update transaction
        transaction.setReturnDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.RETURNED);

        // Update book availability
        transaction.getBook().setAvailable(true);
        bookRepository.save(transaction.getBook());

        // Process insurance refund if eligible
        if (isReturnedOnTime(transaction)) {
            processInsuranceRefund(transaction);
        }

        // Save changes
        BorrowingTransaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Book returned: {}", updatedTransaction);

        return BorrowingTransactionResponseDTO.fromEntity(updatedTransaction);
    }

    private void processInsuranceRefund(BorrowingTransaction transaction) {
        BigDecimal insuranceFee = transaction.getBook().getInsuranceFees();

        TransactionRequestDTO refundRequest = new TransactionRequestDTO();
        refundRequest.setTransactionAmount(insuranceFee);
        refundRequest.setTransactionDate(LocalDateTime.now());
        refundRequest.setTransactionType(TransactionType.CREDIT);
        refundRequest.setCardNumberHash(transaction.getBorrower().getCardNumberHash());

        TransactionResponseDTO refundResponse = cmsClient.processTransaction(refundRequest);
        transaction.setInsuranceRefunded(refundResponse.getStatus() == Status.SUCCESS);

        if (transaction.isInsuranceRefunded()) {
            sendRefundNotification(transaction.getBorrower(), insuranceFee);
        } else {
            log.warn("Insurance refund failed for transaction {}: {}",
                    transaction.getId(), refundResponse.getMessage());
        }
    }

    private boolean isReturnedOnTime(BorrowingTransaction transaction) {
        return LocalDateTime.now().isBefore(transaction.getDueDate());
    }

    private void sendBorrowConfirmation(Borrower borrower, Book book, BigDecimal amount) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(borrower.getEmail());
        emailRequest.setMessage(String.format(
                "You have successfully borrowed '%s'. Amount charged: %s",
                book.getTitle(),
                amount.toString()
        ));

        try {
            emailClient.sendEmail(emailRequest);
        } catch (Exception e) {
            log.error("Failed to send borrow confirmation email", e);
        }
    }

    private void sendRefundNotification(Borrower borrower, BigDecimal amount) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(borrower.getEmail());
        emailRequest.setMessage(String.format(
                "Your insurance fee of %s has been refunded successfully.",
                amount.toString()
        ));

        try {
            emailClient.sendEmail(emailRequest);
        } catch (Exception e) {
            log.error("Failed to send refund notification email", e);
        }
    }
    public List<BorrowingTransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(BorrowingTransactionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public BorrowingTransactionResponseDTO getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(BorrowingTransactionResponseDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public BorrowingTransactionResponseDTO createTransaction(BorrowingTransactionRequestDTO requestDTO) {
        Book book = bookRepository.findById(requestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Borrower borrower = borrowerRepository.findById(requestDTO.getBorrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));

        BorrowingTransaction transaction = new BorrowingTransaction();
        transaction.setBook(book);
        transaction.setBorrower(borrower);
        transaction.setBorrowDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.BORROWED);

        BorrowingTransaction savedTransaction = transactionRepository.save(transaction);
        return BorrowingTransactionResponseDTO.fromEntity(savedTransaction);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }

    public List<BorrowingTransactionResponseDTO> findByBorrower(Long borrowerId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));

        return transactionRepository.findByBorrower(borrower).stream()
                .map(BorrowingTransactionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BorrowingTransactionResponseDTO> findByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        return transactionRepository.findByBook(book).stream()
                .map(BorrowingTransactionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    public List<BorrowingTransactionResponseDTO> findOverdueTransactions() {
        LocalDateTime now = LocalDateTime.now();
        List<BorrowingTransaction> overdueTransactions = transactionRepository
                .findByDueDateBeforeAndStatus(now, TransactionStatus.BORROWED);

        return overdueTransactions.stream()
                .map(BorrowingTransactionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}