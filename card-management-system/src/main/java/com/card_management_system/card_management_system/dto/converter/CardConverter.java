package com.card_management_system.card_management_system.dto.converter;

import com.card_management_system.card_management_system.dto.CardRequestDTO;
import com.card_management_system.card_management_system.dto.CardResponseDTO;
import com.card_management_system.card_management_system.model.Card;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardConverter {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMappings() {
        modelMapper.typeMap(Card.class, CardResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getAccount().getId(),
                            CardResponseDTO::setAccountId);
                });
    }

    public CardResponseDTO toDto(Card card) {
        if (card == null) return null;
        return modelMapper.map(card, CardResponseDTO.class);
    }

    public Card toEntity(CardRequestDTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, Card.class);
    }
}