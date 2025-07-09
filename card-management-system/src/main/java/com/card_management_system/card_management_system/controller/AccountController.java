package com.card_management_system.card_management_system.controller;

import com.card_management_system.card_management_system.dto.AccountRequestDTO;
import com.card_management_system.card_management_system.dto.AccountResponseDTO;
import com.card_management_system.card_management_system.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(
            @Valid @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.status(201).body(accountService.createAccount(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable UUID id,
            @Valid @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.ok(accountService.updateAccount(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
}
