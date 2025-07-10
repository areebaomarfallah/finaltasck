package com.card_management_system.card_management_system.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {

        super(message);
    }

}