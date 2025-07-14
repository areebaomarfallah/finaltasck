package com.card_management_system.card_management_system.dto.converter;

import com.card_management_system.card_management_system.dto.CardRequestDTO;
import com.card_management_system.card_management_system.dto.CardResponseDTO;
import com.card_management_system.card_management_system.model.Card;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CardConverter {
    private final ModelMapper modelMapper;

    public CardResponseDTO toDto(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        CardResponseDTO dto = modelMapper.map(card, CardResponseDTO.class);
        dto.setCardNumber(maskCardNumber(card.getCardNumberHash())); // Still return masked number
        dto.setAccountId(card.getAccount() != null ? card.getAccount().getId() : null);
        return dto;
    }

    public Card toEntity(CardRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CardRequestDTO cannot be null");
        }

        Card card = modelMapper.map(dto, Card.class);
        card.setCardNumberHash(null);
        return card;
    }
    private String maskCardNumber(String cardNumber) {
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }

}
