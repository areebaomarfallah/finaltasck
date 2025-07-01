package com.card_management_system.card_management_system.model;

import com.card_management_system.card_management_system.enume.Status_type;
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
    private Status_type status;

    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    private List<Card> cards;
}