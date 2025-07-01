package com.card_management_system.card_management_system.Exception;

/**
 * Exception thrown when a card number fails format validation
 */
public class CardNumberFormatException extends RuntimeException {

    private final String invalidCardNumber;

    public CardNumberFormatException(String message) {
        super(message);
        this.invalidCardNumber = null;
    }

    public CardNumberFormatException(String message, String invalidCardNumber) {
        super(message);
        this.invalidCardNumber = invalidCardNumber;
    }

    public String getInvalidCardNumber() {
        return invalidCardNumber;
    }

    @Override
    public String getMessage() {
        if (invalidCardNumber != null) {
            return super.getMessage() + " [Card Number: " + maskCardNumber(invalidCardNumber) + "]";
        }
        return super.getMessage();
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}