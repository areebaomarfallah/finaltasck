package com.card_management_system.card_management_system.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;



@Component
public class HashUtil {
    private static final int BCRYPT_STRENGTH = 12;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String cleanCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        // Remove all non-digit characters and trim whitespace
        return cardNumber.replaceAll("[^0-9]", "").trim();
    }

    public String hashCardNumber(String cardNumber) {
        String cleaned = cleanCardNumber(cardNumber);
        return BCrypt.hashpw(cleaned, BCrypt.gensalt(BCRYPT_STRENGTH, SECURE_RANDOM));
    }

    public boolean verifyCardNumber(String inputCardNumber, String hashedCardNumber) {
        try {
            String cleanedInput = cleanCardNumber(inputCardNumber);
            return BCrypt.checkpw(cleanedInput, hashedCardNumber);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidCardNumberFormat(String cardNumber) {
        String cleaned = cleanCardNumber(cardNumber);
        return cleaned.length() >= 13 && cleaned.length() <= 19;
    }
}