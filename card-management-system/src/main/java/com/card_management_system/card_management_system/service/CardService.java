package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.enume.Status_type;
import com.card_management_system.card_management_system.Exception.CardNotFoundException;
import com.card_management_system.card_management_system.Exception.InvalidCardStatusException;
import com.card_management_system.card_management_system.model.*;
import com.card_management_system.card_management_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final AccountService accountService;

    @Autowired
    public CardService(CardRepository cardRepository, AccountService accountService) {
        this.cardRepository = cardRepository;
        this.accountService = accountService;
    }

    // Required by CardController
    public Card createCard(Card card) {
        if (card.getExpiry() == null) {
            card.setExpiry(LocalDate.now().plusYears(2)); // Default 2 year expiry
        }
        card.setStatus(Status_type.INACTIVE); // New cards are inactive by default
        return cardRepository.save(card);
    }

    // Required by CardController
    public Card updateStatus(UUID cardId, Status_type status) {
        Card card = getCard(cardId);

        // Validate status transition
        if (card.getStatus() == Status_type.INACTIVE && status == Status_type.ACTIVE) {
            if (card.getExpiry().isBefore(LocalDate.now())) {
                throw new InvalidCardStatusException("Cannot activate expired card");
            }
        }

        card.setStatus(status);
        return cardRepository.save(card);
    }

    // Required by CardController
    public Card getCard(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
    }

    // Existing methods
    public Card getCardByHash(String cardNumberHash) {
        return cardRepository.findByCardNumber(cardNumberHash)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    public boolean isCardValid(Card card) {
        return card != null &&
                card.getStatus() == Status_type.ACTIVE &&
                !card.getExpiry().isBefore(LocalDate.now());
    }
}