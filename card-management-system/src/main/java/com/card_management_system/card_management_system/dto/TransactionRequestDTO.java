package com.card_management_system.card_management_system.dto;

import com.card_management_system.card_management_system.enume.TransactionType;
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