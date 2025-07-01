package com.card_management_system.card_management_system.dto;

import com.card_management_system.card_management_system.enume.Status;
import com.card_management_system.card_management_system.enume.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponseDTO {
    private UUID id;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    private Status status;
    private String message;
    private UUID cardId;
}