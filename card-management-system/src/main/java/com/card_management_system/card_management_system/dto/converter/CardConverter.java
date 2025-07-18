package com.card_management_system.card_management_system.dto.converter;

import com.card_management_system.card_management_system.dto.CardRequestDTO;
import com.card_management_system.card_management_system.dto.CardResponseDTO;
import com.card_management_system.card_management_system.model.Card;
import com.card_management_system.card_management_system.utils.CardUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardConverter {

    private final ModelMapper modelMapper;
    private final CardUtil cardUtil;

    public CardResponseDTO toDto(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        return modelMapper.map(card, CardResponseDTO.class);
    }

    public Card toEntity(CardRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CardRequestDTO cannot be null");
        }
        return modelMapper.map(dto, Card.class);
    }
}