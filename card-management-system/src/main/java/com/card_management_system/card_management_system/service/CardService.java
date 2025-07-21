package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.dto.CardRequestDTO;
import com.card_management_system.card_management_system.dto.CardResponseDTO;
import com.card_management_system.card_management_system.exception.CardNotFoundException;
import com.card_management_system.card_management_system.exception.InvalidCardStatusException;
import com.card_management_system.card_management_system.model.Card;
import com.card_management_system.card_management_system.repository.CardRepository;
import com.card_management_system.card_management_system.dto.converter.CardConverter;
import com.card_management_system.card_management_system.utils.CommonEnum;
import com.card_management_system.card_management_system.utils.CardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final CardConverter cardConverter;
    private final CardUtil cardUtil;

    public CardResponseDTO createCard(CardRequestDTO dto) {
        if (!accountService.accountExists(dto.getAccountId())) {
            throw new IllegalArgumentException("Account does not exist");
        }

        // Generate card details
        String generatedCardNumber = cardUtil.generateValidCardNumber();
        String lastFour = generatedCardNumber.substring(generatedCardNumber.length() - 4);

        // Create entity
        Card card = cardConverter.toEntity(dto);
        card.setCardNumberHash(cardUtil.hashCardNumber(generatedCardNumber));
        card.setLastFourDigits(lastFour);
        card.setExpiry(LocalDate.now().plusYears(5));
        card.setAccount(accountService.getAccountEntity(dto.getAccountId()));

        // Set default status if null
        if (card.getStatus() == null) {
            card.setStatus(CommonEnum.StatusType.INACTIVE);
        }

        // Save and convert
        return cardConverter.toDto(cardRepository.save(card));
    }

    public CardResponseDTO getCardById(UUID id) {
        return cardConverter.toDto(getCardEntity(id));
    }
    private CardResponseDTO convertToResponseDTO(Card card) {
        CardResponseDTO response = new CardResponseDTO();
        response.setId(card.getId());
        response.setStatus(card.getStatus());
        response.setExpiry(card.getExpiry());
        response.setCardNumberHash(card.getCardNumberHash());
        response.setLastFourDigits(card.getLastFourDigits());
        response.setAccountId(card.getAccount().getId());
        return response;
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




    private Card getCardEntity(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
    }

    public boolean isCardValid(UUID cardId) {
        Card card = getCardEntity(cardId);
        return card.getStatus() == CommonEnum.StatusType.ACTIVE &&
                !card.getExpiry().isBefore(LocalDate.now());
    }
    public Optional<Card> findByCardNumberHash(String cardNumberHash) {
        return cardRepository.findByCardNumberHash(cardNumberHash);
    }

}