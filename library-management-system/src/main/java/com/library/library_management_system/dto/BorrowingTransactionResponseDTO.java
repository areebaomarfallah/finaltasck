package com.library.library_management_system.dto;

import com.library.library_management_system.emun.TransactionStatus;
import com.library.library_management_system.model.BorrowingTransaction;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class BorrowingTransactionResponseDTO {
    private Long id;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private LocalDateTime dueDate;
    private TransactionStatus status;
    private Long bookId;
    private String bookTitle;
    private Long borrowerId;
    private String borrowerName;
    private BigDecimal transactionAmount;
    private boolean insuranceRefunded;

    public static BorrowingTransactionResponseDTO fromEntity(BorrowingTransaction transaction) {
        BorrowingTransactionResponseDTO dto = new BorrowingTransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setBorrowDate(transaction.getBorrowDate());
        dto.setReturnDate(transaction.getReturnDate());
        dto.setDueDate(transaction.getDueDate());
        dto.setStatus(transaction.getStatus());
        dto.setBookId(transaction.getBook().getId());
        dto.setBookTitle(transaction.getBook().getTitle());
        dto.setBorrowerId(transaction.getBorrower().getId());
        dto.setBorrowerName(transaction.getBorrower().getName());
        dto.setTransactionAmount(transaction.getTransactionAmount());
        dto.setInsuranceRefunded(transaction.isInsuranceRefunded());
        return dto;
    }
}