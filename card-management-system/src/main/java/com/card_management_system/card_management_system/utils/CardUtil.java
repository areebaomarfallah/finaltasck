package com.card_management_system.card_management_system.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class CardUtil {
    private static final int BCRYPT_STRENGTH = 12;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Random RANDOM = new Random();
    private static final int[] PREFIXES = {4, 5, 6}; // Visa, Mastercard, Discover prefixes

    public String cleanCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        return cardNumber.replaceAll("[^0-9]", "").trim();
    }

    public String hashCardNumber(String cardNumber) {
        return BCrypt.hashpw(cleanCardNumber(cardNumber), BCrypt.gensalt(BCRYPT_STRENGTH, SECURE_RANDOM));
    }

    public boolean verifyCardNumber(String inputCardNumber, String hashedCardNumber) {
        try {
            return BCrypt.checkpw(cleanCardNumber(inputCardNumber), hashedCardNumber);
        } catch (Exception e) {
            return false;
        }
    }


    public String generateValidCardNumber() {
        int length = 16; // Standard card number length
        int prefix = PREFIXES[RANDOM.nextInt(PREFIXES.length)];

        StringBuilder cardNumber = new StringBuilder(String.valueOf(prefix));
        while (cardNumber.length() < length - 1) {
            cardNumber.append(RANDOM.nextInt(10));
        }

        int checkDigit = calculateLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);

        return cardNumber.toString();
    }

    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (sum % 10) == 0;
    }

    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }

    public String getLastFourDigits(String hashedCardNumber, String inputCardNumber) {
        if (!verifyCardNumber(inputCardNumber, hashedCardNumber)) {
            throw new IllegalArgumentException("Card number verification failed");
        }
        String cleaned = cleanCardNumber(inputCardNumber);
        return cleaned.substring(cleaned.length() - 4);
    }
}