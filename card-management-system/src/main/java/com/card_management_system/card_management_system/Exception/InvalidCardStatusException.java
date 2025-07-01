package com.card_management_system.card_management_system.Exception;

import com.card_management_system.card_management_system.enume.Status_type;

import java.util.UUID;

/**
 * Exception thrown when an invalid card status transition is attempted
 */
public class InvalidCardStatusException extends RuntimeException {

    private final Status_type currentStatus;
    private final Status_type attemptedStatus;
    private final UUID cardId;

    public InvalidCardStatusException(String message) {
        super(message);
        this.currentStatus = null;
        this.attemptedStatus = null;
        this.cardId = null;
    }

    public InvalidCardStatusException(String message,
                                      Status_type currentStatus,
                                      Status_type attemptedStatus,
                                      UUID cardId) {
        super(message);
        this.currentStatus = currentStatus;
        this.attemptedStatus = attemptedStatus;
        this.cardId = cardId;
    }

    public Status_type getCurrentStatus() {
        return currentStatus;
    }

    public Status_type getAttemptedStatus() {
        return attemptedStatus;
    }

    public UUID getCardId() {
        return cardId;
    }

    @Override
    public String getMessage() {
        String baseMessage = super.getMessage();
        if (currentStatus != null && attemptedStatus != null && cardId != null) {
            return String.format("%s [Current Status: %s, Attempted Status: %s, Card ID: %s]",
                    baseMessage, currentStatus, attemptedStatus, cardId);
        }
        return baseMessage;
    }
}