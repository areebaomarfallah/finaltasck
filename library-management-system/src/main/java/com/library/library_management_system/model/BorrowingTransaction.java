package com.library.library_management_system.model;

import com.library.library_management_system.utils.CommonEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "borrowing_transactions")
public class BorrowingTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    private UUID id;

    @Column(name = "borrow_date", nullable = false)
    private LocalDateTime borrowDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CommonEnum.TransactionStatus status;

    @Column(name = "book_id", nullable = false)
    private UUID bookId;

    @Column(name = "borrower_id", nullable = false)
    private UUID borrowerId;

    @Column(name = "transaction_amount", precision = 10, scale = 2)
    private BigDecimal transactionAmount;

    @Column(name = "cms_transaction_id")
    private UUID cmsTransactionId;

    @Column(name = "insurance_refunded", nullable = false)
    private boolean insuranceRefunded = false;
}