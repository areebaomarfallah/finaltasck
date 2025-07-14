package com.card_management_system.card_management_system.dto.converter;

import com.card_management_system.card_management_system.dto.TransactionRequestDTO;
import com.card_management_system.card_management_system.dto.TransactionResponseDTO;
import com.card_management_system.card_management_system.model.Card;
import com.card_management_system.card_management_system.model.Transaction;
import com.card_management_system.card_management_system.utils.CommonEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransactionConverter {

    private final ModelMapper modelMapper;


    public TransactionResponseDTO toDto(Transaction transaction) {
        TransactionResponseDTO dto = modelMapper.map(transaction, TransactionResponseDTO.class);
        dto.setCardId(transaction.getCard().getId());
        return dto;
    }

    public Transaction toEntity(TransactionRequestDTO request, Card card) {
        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(request.getTransactionAmount());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCard(card);
        transaction.setStatus(CommonEnum.Status.SUCCESS);
        return transaction;
    }
}
