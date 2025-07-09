package com.card_management_system.card_management_system.model;

import com.card_management_system.card_management_system.utils.CommonEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonEnum.StatusType status;

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;
}