package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.exception.CardNotFoundException;
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

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardService cardService;
    private final AccountService accountService;
    private final TransactionConverter transactionConverter;
    private final HashUtil hashUtil;

    public TransactionResponseDTO processTransaction(TransactionRequestDTO request) {
        try {
            // Clean the card number by removing non-digit characters
            String cleanedCardNumber = request.getCardNumber().replaceAll("[^0-9]", "");

            // Find card by verifying against all cards
            Card card = cardService.findCardByNumberVerification(cleanedCardNumber)
                    .orElseThrow(() -> new InvalidTransactionException("Card not found"));

            // Verify card number matches stored hash
            if (!hashUtil.verifyCardNumber(cleanedCardNumber, card.getCardNumberHash())) {
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

            Transaction transaction = transactionConverter.toEntity(request, card);

            if (request.getTransactionType() == CommonEnum.TransactionType.DEBIT) {
                account.setBalance(account.getBalance().subtract(request.getTransactionAmount()));
            } else {
                account.setBalance(account.getBalance().add(request.getTransactionAmount()));
            }

            return transactionConverter.toDto(transactionRepository.save(transaction));

        } catch (InvalidTransactionException | InsufficientFundsException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            throw new InvalidTransactionException("Transaction processing failed: " + e.getMessage());
        }
    }
}