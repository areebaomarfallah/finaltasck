package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.enume.*;
import com.card_management_system.card_management_system.model.*;
import com.card_management_system.card_management_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    public Transaction processTransaction(Transaction transaction) {
        Card card = transaction.getCard();
        Account account = card.getAccount();

        // Validate card and account
        validateTransaction(transaction, card, account);

        // Process transaction
        if (transaction.getTransactionType() == TransactionType.DEBIT) {
            account.setBalance(account.getBalance().subtract(transaction.getTransactionAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getTransactionAmount()));
        }

        transaction.setStatus(Status.SUCCESS);
        return transactionRepository.save(transaction);
    }

    private void validateTransaction(Transaction transaction, Card card, Account account) {
        if (!cardService.isCardValid(card)) {
            throw new RuntimeException("Invalid card");
        }

        if (!accountService.isAccountActive(account)) {
            throw new RuntimeException("Inactive account");
        }

        if (transaction.getTransactionType() == TransactionType.DEBIT &&
                !accountService.hasSufficientBalance(account, transaction.getTransactionAmount())) {
            throw new RuntimeException("Insufficient funds");
        }
    }
}