package com.library.library_management_system.dto;

import com.library.library_management_system.emun.TransactionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowingTransactionRequestDTO {
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private TransactionStatus status;
    private Long bookId;
    private Long borrowerId;
}
