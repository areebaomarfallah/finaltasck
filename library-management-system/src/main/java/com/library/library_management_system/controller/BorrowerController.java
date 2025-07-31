package com.library.library_management_system.controller;

import com.library.library_management_system.dto.*;
import com.library.library_management_system.service.BorrowerService;
import com.library.library_management_system.utils.CommonEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrowers")
public class BorrowerController {
    private final BorrowerService borrowerService;

    @GetMapping
    public ResponseEntity<List<BorrowerResponseDTO>> getAllBorrowers() {
        return ResponseEntity.ok(borrowerService.getAllBorrowers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponseDTO> getBorrowerById(@PathVariable UUID id) {
        return ResponseEntity.ok(borrowerService.getBorrowerById(id));
    }

    @PostMapping
    public ResponseEntity<BorrowerResponseDTO> createBorrower(@Valid @RequestBody BorrowerRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowerService.createBorrower(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowerResponseDTO> updateBorrower(
            @PathVariable UUID id,
            @Valid @RequestBody BorrowerRequestDTO requestDTO) {
        return ResponseEntity.ok(borrowerService.updateBorrower(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable UUID id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/name")
    public ResponseEntity<String> getBorrowerName(@PathVariable UUID id) {
        return ResponseEntity.ok(borrowerService.getBorrowerName(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateBorrowerStatus(
            @PathVariable UUID id,
            @RequestParam CommonEnum.AccountStatus status) {
        borrowerService.updateBorrowerStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{borrowerId}/transactions/{transactionId}")
    public ResponseEntity<Void> addTransactionToBorrower(
            @PathVariable UUID borrowerId,
            @PathVariable UUID transactionId) {
        borrowerService.addTransactionToBorrower(borrowerId, transactionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{borrowerId}/transactions/{transactionId}")
    public ResponseEntity<Void> removeTransactionFromBorrower(
            @PathVariable UUID borrowerId,
            @PathVariable UUID transactionId) {
        borrowerService.removeTransactionFromBorrower(borrowerId, transactionId);
        return ResponseEntity.noContent().build();
    }
}