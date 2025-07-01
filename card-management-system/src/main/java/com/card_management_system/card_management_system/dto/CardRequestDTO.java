package com.card_management_system.card_management_system.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CardRequestDTO {
    private String status;
    private LocalDate expiry;
    private String cardNumber;
    private UUID accountId; // To associate card with an account

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // getters and setters
}

