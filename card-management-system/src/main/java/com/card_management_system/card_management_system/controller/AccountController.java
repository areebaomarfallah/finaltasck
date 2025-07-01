package com.card_management_system.card_management_system.controller;

import com.card_management_system.card_management_system.dto.AccountRequestDTO;
import com.card_management_system.card_management_system.dto.AccountResponseDTO;
import com.card_management_system.card_management_system.enume.Status_type;
import com.card_management_system.card_management_system.model.Account;
import com.card_management_system.card_management_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO dto) {
        Account account = mapToEntity(dto);
        Account savedAccount = accountService.createAccount(account);
        return ResponseEntity.status(201).body(mapToResponseDTO(savedAccount));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable UUID id, @RequestBody AccountRequestDTO dto) {
        Account account = mapToEntity(dto);
        account.setId(id);
        Account updatedAccount = accountService.updateAccount(account);
        return ResponseEntity.ok(mapToResponseDTO(updatedAccount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable UUID id) {
        Account account = accountService.getAccount(id);
        return ResponseEntity.ok(mapToResponseDTO(account));
    }

    private AccountResponseDTO mapToResponseDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setStatus(account.getStatus().name());
        dto.setBalance(account.getBalance());
        return dto;
    }

    private Account mapToEntity(AccountRequestDTO dto) {
        Account account = new Account();
        account.setStatus(Status_type.valueOf(dto.getStatus()));
        account.setBalance(dto.getBalance());
        return account;
    }
}
