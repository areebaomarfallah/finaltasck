package com.library.library_management_system.dto;

import com.library.library_management_system.emun.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRequestDTO {
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    private String cardNumberHash;
}