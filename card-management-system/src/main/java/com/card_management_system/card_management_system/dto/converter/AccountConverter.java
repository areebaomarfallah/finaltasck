package com.card_management_system.card_management_system.dto.converter;

import com.card_management_system.card_management_system.dto.AccountRequestDTO;
import com.card_management_system.card_management_system.dto.AccountResponseDTO;
import com.card_management_system.card_management_system.model.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AccountConverter {

    private final ModelMapper modelMapper;

    public AccountResponseDTO toDto(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        return modelMapper.map(account, AccountResponseDTO.class);
    }
    public Account toEntity(AccountRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("AccountRequestDTO cannot be null");
        }

        // Add null check for balance before comparison
        if (dto.getBalance() == null) {
            throw new IllegalArgumentException("Account balance cannot be null");
        }

        if (dto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Account balance cannot be negative");
        }


        return modelMapper.map(dto, Account.class);
    }


}
