package com.card_management_system.card_management_system.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountResponseDTO {
    private UUID id;
    private String status;
    private BigDecimal balance;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
// getters and setters
}
