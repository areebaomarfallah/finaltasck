package com.card_management_system.card_management_system.model;

import com.card_management_system.card_management_system.enume.Status_type;
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

    private String cardNumber;
    private LocalDate expiry;

    @Enumerated(EnumType.STRING)
    private Status_type status;

    @ManyToOne
    private Account account;
}