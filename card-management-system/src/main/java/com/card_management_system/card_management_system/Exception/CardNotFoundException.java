package com.card_management_system.card_management_system.Exception;

import java.util.UUID;

/**
 * Exception thrown when a card cannot be found in the system
 */
public class CardNotFoundException extends RuntimeException {

    private final UUID cardId;

    public CardNotFoundException(UUID cardId) {
        super("Card not found");
        this.cardId = cardId;
    }

    public CardNotFoundException(UUID cardId, String message) {
        super(message);
        this.cardId = cardId;
    }

    public UUID getCardId() {
        return cardId;
    }

    @Override
    public String getMessage() {
        return String.format("%s [Card ID: %s]", super.getMessage(), cardId);
    }
}