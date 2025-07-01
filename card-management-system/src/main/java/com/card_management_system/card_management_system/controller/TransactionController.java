package com.card_management_system.card_management_system.controller;

import com.card_management_system.card_management_system.dto.*;
import com.card_management_system.card_management_system.enume.*;
import com.card_management_system.card_management_system.model.*;
import com.card_management_system.card_management_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> processTransaction(
            @RequestBody TransactionRequestDTO request) {

        Transaction transaction = mapToEntity(request);
        Transaction processedTransaction = transactionService.processTransaction(transaction);
        return ResponseEntity.ok(mapToDTO(processedTransaction));
    }

    private Transaction mapToEntity(TransactionRequestDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(dto.getTransactionAmount());
        transaction.setTransactionType(dto.getTransactionType());

        // Find card by hash
        Card card = cardService.getCardByHash(dto.getCardNumberHash());
        transaction.setCard(card);

        return transaction;
    }

    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setTransactionAmount(transaction.getTransactionAmount());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setStatus(transaction.getStatus());
        dto.setMessage("Transaction processed");
        return dto;
    }
}