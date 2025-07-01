package com.card_management_system.card_management_system.controller;

import com.card_management_system.card_management_system.dto.*;
import com.card_management_system.card_management_system.enume.Status_type;
import com.card_management_system.card_management_system.model.*;
import com.card_management_system.card_management_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired private CardService cardService;
    @Autowired private AccountService accountService;

    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(@RequestBody CardRequestDTO dto) {
        Card card = mapToEntity(dto);
        Card savedCard = cardService.createCard(card);
        return ResponseEntity.status(201).body(mapToResponseDTO(savedCard));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CardResponseDTO> updateCardStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        Card updatedCard = cardService.updateStatus(id, Status_type.valueOf(status));
        return ResponseEntity.ok(mapToResponseDTO(updatedCard));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDTO> getCard(@PathVariable UUID id) {
        Card card = cardService.getCard(id);
        return ResponseEntity.ok(mapToResponseDTO(card));
    }

    private CardResponseDTO mapToResponseDTO(Card card) {
        CardResponseDTO dto = new CardResponseDTO();
        dto.setId(card.getId());
        dto.setStatus(card.getStatus().name());
        dto.setExpiry(card.getExpiry());
        dto.setCardNumber("****-****-****-" + card.getCardNumber().substring(card.getCardNumber().length() - 4));
        dto.setAccountId(card.getAccount() != null ? card.getAccount().getId() : null);
        return dto;
    }

    private Card mapToEntity(CardRequestDTO dto) {
        Card card = new Card();
        card.setStatus(Status_type.valueOf(dto.getStatus()));
        card.setExpiry(dto.getExpiry());
        card.setCardNumber(dto.getCardNumber());

        if (dto.getAccountId() != null) {
            Account account = accountService.getAccount(dto.getAccountId());
            card.setAccount(account);
        }
        return card;
    }
}