package com.card_management_system.card_management_system.exception;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String message) {
        super(message);
    }
}