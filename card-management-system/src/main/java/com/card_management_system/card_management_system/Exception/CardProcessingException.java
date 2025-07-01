package com.card_management_system.card_management_system.Exception;
import java.util.UUID;

/**
 * Exception thrown when an error occurs during card processing operations
 */
public class CardProcessingException extends RuntimeException {

    private final UUID cardId;
    private final String operation;

    public CardProcessingException(String message) {
        super(message);
        this.cardId = null;
        this.operation = null;
    }

    public CardProcessingException(String message, Throwable cause) {
        super(message, cause);
        this.cardId = null;
        this.operation = null;
    }

    public CardProcessingException(String message, UUID cardId, String operation) {
        super(message);
        this.cardId = cardId;
        this.operation = operation;
    }

    public CardProcessingException(String message, Throwable cause, UUID cardId, String operation) {
        super(message, cause);
        this.cardId = cardId;
        this.operation = operation;
    }

    public UUID getCardId() {
        return cardId;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String getMessage() {
        String baseMessage = super.getMessage();
        if (cardId != null && operation != null) {
            return String.format("%s [Card ID: %s, Operation: %s]",
                    baseMessage, cardId, operation);
        }
        return baseMessage;
    }
}