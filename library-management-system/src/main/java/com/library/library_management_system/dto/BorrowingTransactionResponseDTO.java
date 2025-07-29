// BorrowingTransactionResponseDTO.java
package com.library.library_management_system.dto;

import com.library.library_management_system.utils.CommonEnum;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class BorrowingTransactionResponseDTO {
    private UUID id;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private LocalDateTime dueDate;
    private CommonEnum.TransactionStatus status;
    private UUID bookId;
    private String bookTitle;
    private UUID borrowerId;
    private String borrowerName;
    private BigDecimal transactionAmount;
    private boolean insuranceRefunded;
}