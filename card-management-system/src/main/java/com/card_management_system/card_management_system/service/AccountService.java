package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.enume.Status_type;
import com.card_management_system.card_management_system.model.Account;
import com.card_management_system.card_management_system.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        account.setStatus(Status_type.ACTIVE); // Use enum constant
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        return accountRepository.save(account);
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccount(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public boolean isAccountActive(Account account) {
        return account.getStatus() == Status_type.ACTIVE;
    }

    public boolean hasSufficientBalance(Account account, BigDecimal amount) {
        return account.getBalance().compareTo(amount) >= 0;
    }
}
