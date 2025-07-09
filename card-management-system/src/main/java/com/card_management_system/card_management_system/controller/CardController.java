package com.card_management_system.card_management_system.controller;

import com.card_management_system.card_management_system.dto.CardRequestDTO;
import com.card_management_system.card_management_system.dto.CardResponseDTO;
import com.card_management_system.card_management_system.service.CardService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(
          @RequestBody CardRequestDTO dto) {
        return ResponseEntity.status(201).body(cardService.createCard(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CardResponseDTO> updateCardStatus(
            @PathVariable UUID id,
            @RequestParam @NotBlank String status) {
        return ResponseEntity.ok(cardService.updateCardStatus(id, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDTO> getCard(@PathVariable UUID id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }
}
