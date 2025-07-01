package com.card_management_system.card_management_system.model;

import com.card_management_system.card_management_system.enume.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private Card card;
}