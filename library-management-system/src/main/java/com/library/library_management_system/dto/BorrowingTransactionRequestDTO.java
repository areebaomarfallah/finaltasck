// BorrowingTransactionRequestDTO.java
package com.library.library_management_system.dto;

import com.library.library_management_system.utils.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class BorrowingTransactionRequestDTO {
    @NotNull(message = "Borrow date is required")
    private LocalDateTime borrowDate;
    
    private LocalDateTime returnDate;
    
    @NotNull(message = "Transaction status is required")
    private CommonEnum.TransactionStatus status;
    
    @NotNull(message = "Book ID is required")
    private UUID bookId;
    
    @NotNull(message = "Borrower ID is required")
    private UUID borrowerId;
}