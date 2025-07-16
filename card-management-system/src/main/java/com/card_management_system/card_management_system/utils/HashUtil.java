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

    public boolean isValidCardNumberFormat(String cardNumber) {

        return cleanCardNumber(cardNumber).length() >= 13 && cleanCardNumber(cardNumber).length() <= 19;

    }
}