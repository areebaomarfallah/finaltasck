package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.exception.InsufficientFundsException;
import com.card_management_system.card_management_system.exception.InvalidTransactionException;
import com.card_management_system.card_management_system.dto.TransactionRequestDTO;
import com.card_management_system.card_management_system.dto.TransactionResponseDTO;

import com.card_management_system.card_management_system.model.*;
import com.card_management_system.card_management_system.repository.TransactionRepository;
import com.card_management_system.card_management_system.dto.converter.TransactionConverter;
import com.card_management_system.card_management_system.utils.CommonEnum;
import com.card_management_system.card_management_system.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public  class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardService cardService;
    private final AccountService accountService;
    private final TransactionConverter transactionConverter;
    public TransactionResponseDTO processTransaction(TransactionRequestDTO request) {

        Card card = cardService.getCardByHash(request.getCardNumberHash());

        if (!HashUtil.verifyCardNumber(request.getCardNumberHash(), card.getCardNumberHash())) {
            throw new InvalidTransactionException("Card verification failed");
        }
        Account account = card.getAccount();

        if (!cardService.isCardValid(card.getId())) {
            throw new InvalidTransactionException("Invalid card");
        }
        if (!accountService.isAccountActive(account.getId())) {
            throw new InvalidTransactionException("Inactive account");
        }
        if (request.getTransactionType() == CommonEnum.TransactionType.DEBIT &&
                !accountService.hasSufficientBalance(account.getId(), request.getTransactionAmount())) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(request.getTransactionAmount());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCard(card);
        transaction.setStatus(CommonEnum.Status.SUCCESS);

        if (request.getTransactionType() == CommonEnum.TransactionType.DEBIT) {
            account.setBalance(account.getBalance().subtract(request.getTransactionAmount()));
        } else {
            account.setBalance(account.getBalance().add(request.getTransactionAmount()));
        }

        return transactionConverter.toDto(transactionRepository.save(transaction));
    }


}