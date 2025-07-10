package com.card_management_system.card_management_system.exception;



public class CardNumberFormatException extends RuntimeException {

    public CardNumberFormatException(String message, String invalidCardNumber) {
        super(String.format("%s [Card Number: %s]",
                message, maskCardNumber(invalidCardNumber)));
    }

    private static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}