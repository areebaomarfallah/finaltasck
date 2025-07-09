package com.card_management_system.card_management_system.model;

import com.card_management_system.card_management_system.utils.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal transactionAmount;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonEnum.TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonEnum.Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
}