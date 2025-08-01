package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.exception.InsufficientFundsException;
import com.card_management_system.card_management_system.exception.InvalidTransactionException;
import com.card_management_system.card_management_system.dto.TransactionRequestDTO;
import com.card_management_system.card_management_system.dto.TransactionResponseDTO;
import com.card_management_system.card_management_system.model.*;
import com.card_management_system.card_management_system.repository.CardRepository;
import com.card_management_system.card_management_system.repository.TransactionRepository;
import com.card_management_system.card_management_system.dto.converter.TransactionConverter;
import com.card_management_system.card_management_system.utils.CommonEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardService cardService;
    private final AccountService accountService;
    private final TransactionConverter transactionConverter;

    public TransactionResponseDTO processTransaction(TransactionRequestDTO request) {
        try {
            Card card = cardService.findByCardNumberHash(request.getCardNumberHash())
                    .orElseThrow(() -> new InvalidTransactionException("Card not found"));

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


            if (request.getTransactionType() == CommonEnum.TransactionType.DEBIT) {
                account.setBalance(account.getBalance().subtract(request.getTransactionAmount()));
            } else {
                account.setBalance(account.getBalance().add(request.getTransactionAmount()));
            }

            return transactionConverter.toDto(transactionRepository.save(transactionConverter.toEntity(request, card)));

        } catch (InvalidTransactionException | InsufficientFundsException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidTransactionException("Transaction processing failed: " + e.getMessage());
        }
    }
}