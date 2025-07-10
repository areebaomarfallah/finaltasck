package com.card_management_system.card_management_system.exception;

import java.util.UUID;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(String message) {

        super(message);
    }

    public CardNotFoundException(UUID cardId) {

        super(String.format("Card not found with ID: %s", cardId));
    }


}