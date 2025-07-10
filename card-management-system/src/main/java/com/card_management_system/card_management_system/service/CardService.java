package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.dto.CardRequestDTO;
import com.card_management_system.card_management_system.dto.CardResponseDTO;
import com.card_management_system.card_management_system.exception.CardNotFoundException;
import com.card_management_system.card_management_system.exception.InvalidCardStatusException;
import com.card_management_system.card_management_system.model.Account;
import com.card_management_system.card_management_system.model.Card;
import com.card_management_system.card_management_system.repository.CardRepository;
import com.card_management_system.card_management_system.dto.converter.CardConverter;
import com.card_management_system.card_management_system.utils.CommonEnum;
import com.card_management_system.card_management_system.utils.HashUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public  class CardService {

    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final CardConverter cardConverter;
    public CardResponseDTO createCard(@Valid CardRequestDTO dto) {
        if (!HashUtil.isValidCardNumberFormat(dto.getCardNumber())) {
            throw new IllegalArgumentException("Invalid card number format");
        }

        Card card = cardConverter.toEntity(dto);
        card.setCardNumberHash(HashUtil.hashCardNumber(dto.getCardNumber()));

        if (card.getStatus() == null) {
            card.setStatus(CommonEnum.StatusType.INACTIVE);
        }

        card.setAccount(accountService.getAccountEntity(dto.getAccountId()));

        if (card.getExpiry().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Card expiry date cannot be in the past");
        }

        return cardConverter.toDto(cardRepository.save(card));
    }

    public CardResponseDTO updateCardStatus(UUID cardId, String status) {
        CommonEnum.StatusType newStatus;
        try {
            newStatus = CommonEnum.StatusType.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

        Card card = getCardEntity(cardId);
        validateStatusTransition(card, newStatus);

        card.setStatus(newStatus);
        return cardConverter.toDto(cardRepository.save(card));
    }

    // Enhanced status transition validation
    private void validateStatusTransition(Card card, CommonEnum.StatusType newStatus) {
        if (card.getStatus() == newStatus) {
            return;
        }

        if (newStatus == CommonEnum.StatusType.ACTIVE) {
            if (card.getExpiry().isBefore(LocalDate.now())) {
                throw new InvalidCardStatusException("Cannot activate expired card");
            }
            if (!accountService.isAccountActive(card.getAccount().getId())) {
                throw new InvalidCardStatusException("Cannot activate card for inactive account");
            }
        }
    }

    public CardResponseDTO getCardById(UUID id) {

        return cardConverter.toDto(getCardEntity(id));
    }

    public Card getCardByHash(String cardNumberHash) {
        return cardRepository.findByCardNumberHash(cardNumberHash)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }


    private Card getCardEntity(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
    }

    public boolean isCardValid(UUID cardId) {
        Card card = getCardEntity(cardId);
        return card.getStatus() == CommonEnum.StatusType.ACTIVE &&
                !card.getExpiry().isBefore(LocalDate.now());
    }

}