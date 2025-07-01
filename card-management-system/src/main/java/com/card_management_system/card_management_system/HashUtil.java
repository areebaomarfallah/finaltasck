package com.card_management_system.card_management_system;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;

public class HashUtil {
    private static final int BCRYPT_STRENGTH = 12;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String hashCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        return BCrypt.hashpw(cardNumber, BCrypt.gensalt(BCRYPT_STRENGTH, SECURE_RANDOM));
    }

    public static boolean verifyCardNumber(String cardNumber, String hashedCardNumber) {
        return cardNumber != null &&
                hashedCardNumber != null &&
                BCrypt.checkpw(cardNumber, hashedCardNumber);
    }

    public static boolean isValidCardNumberFormat(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) n = (n % 10) + 1;
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}