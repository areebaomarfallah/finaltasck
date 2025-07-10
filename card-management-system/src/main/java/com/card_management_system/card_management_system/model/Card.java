package com.card_management_system.card_management_system.model;

import com.card_management_system.card_management_system.utils.CommonEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cardNumberHash;

    @Column(nullable = false)
    private LocalDate expiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonEnum.StatusType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}