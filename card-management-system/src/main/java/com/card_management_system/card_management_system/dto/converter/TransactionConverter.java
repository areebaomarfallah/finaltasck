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
        modelMapper.typeMap(Transaction.class, TransactionResponseDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getCard().getId(),
                        TransactionResponseDTO::setCardId));

        return modelMapper.map(transaction, TransactionResponseDTO.class);
    }

    public Transaction toEntity(TransactionRequestDTO request, Card card) {

        modelMapper.typeMap(TransactionRequestDTO.class, Transaction.class)

                .addMappings(mapper -> {
                    mapper.skip(Transaction::setId);
                    mapper.skip(Transaction::setTransactionDate);
                    mapper.skip(Transaction::setStatus);
                });

        Transaction transaction = modelMapper.map(request, Transaction.class);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCard(card);
        transaction.setStatus(CommonEnum.Status.SUCCESS);

        return transaction;
    }
}