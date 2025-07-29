package com.library.library_management_system.model;

import com.library.library_management_system.utils.CommonEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "borrowers")
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "borrower_id")
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Email
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", length = 15)
    @Pattern(regexp = "^\\+?[0-9\\-\\s]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommonEnum.AccountStatus status = CommonEnum.AccountStatus.ACTIVE;

    @Column(name = "card_number_hash", nullable = false, length = 255)
    private String cardNumberHash;

    @ElementCollection
    @CollectionTable(name = "borrower_transactions", joinColumns = @JoinColumn(name = "borrower_id"))
    @Column(name = "transaction_id")
    private List<UUID> transactionIds = new ArrayList<>();
}