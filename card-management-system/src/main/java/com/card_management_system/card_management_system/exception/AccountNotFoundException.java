package com.card_management_system.card_management_system.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(UUID accountId) {
        super(String.format("Account not found with ID: %s", accountId));
    }


}