package com.card_management_system.card_management_system.service;

import com.card_management_system.card_management_system.dto.AccountRequestDTO;
import com.card_management_system.card_management_system.dto.AccountResponseDTO;
import com.card_management_system.card_management_system.exception.AccountNotFoundException;
import com.card_management_system.card_management_system.model.Account;
import com.card_management_system.card_management_system.repository.AccountRepository;
import com.card_management_system.card_management_system.dto.converter.AccountConverter;
import com.card_management_system.card_management_system.utils.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountConverter accountConverter;

    public AccountResponseDTO createAccount(AccountRequestDTO dto) {

        return accountConverter.toDto(accountRepository.save(accountConverter.toEntity(dto)));
    }

    public AccountResponseDTO updateAccount(UUID id, AccountRequestDTO dto) {
        Account account = getAccountEntity(id);

        return accountConverter.toDto(accountRepository.save(account));
    }

    public AccountResponseDTO getAccountById(UUID id) {

        return accountConverter.toDto(getAccountEntity(id));
    }

    public boolean isAccountActive(UUID accountId) {
        return getAccountEntity(accountId).getStatus() == CommonEnum.StatusType.ACTIVE;
    }

    public boolean hasSufficientBalance(UUID accountId, BigDecimal amount) {
        return getAccountEntity(accountId).getBalance().compareTo(amount) >= 0;
    }

    Account getAccountEntity(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }


    public boolean accountExists(UUID accountId) {
        return accountRepository.existsById(accountId);
    }
}
