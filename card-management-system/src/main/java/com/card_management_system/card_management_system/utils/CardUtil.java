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




    public String generateValidCardNumber() {
        int length = 16; // Standard card number length
        int prefix = PREFIXES[RANDOM.nextInt(PREFIXES.length)];

        StringBuilder cardNumber = new StringBuilder(String.valueOf(prefix));
        while (cardNumber.length() < length - 1) {
            cardNumber.append(RANDOM.nextInt(10));
        }

        return cardNumber.toString();
    }

}