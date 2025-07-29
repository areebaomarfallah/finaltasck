package com.library.library_management_system.controller;

import com.library.library_management_system.dto.BorrowingTransactionRequestDTO;
import com.library.library_management_system.dto.BorrowingTransactionResponseDTO;
import com.library.library_management_system.service.BorrowingTransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class BorrowingTransactionController {
    private final BorrowingTransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingTransactionResponseDTO> getTransactionById(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping
    public ResponseEntity<BorrowingTransactionResponseDTO> createTransaction(
            @Valid @RequestBody BorrowingTransactionRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(requestDTO));
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingTransactionResponseDTO> borrowBook(
            @RequestParam UUID bookId,
            @RequestParam UUID borrowerId,
            @RequestParam(defaultValue = "7") @Min(1) int durationDays) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.borrowBook(bookId, borrowerId, durationDays));
    }

    @PostMapping("/return/{transactionId}")
    public ResponseEntity<BorrowingTransactionResponseDTO> returnBook(@PathVariable UUID transactionId) {
        return ResponseEntity.ok(transactionService.returnBook(transactionId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getByBook(@PathVariable UUID bookId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBook(bookId));
    }

    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getByBorrower(@PathVariable UUID borrowerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBorrower(borrowerId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getOverdueTransactions() {
        return ResponseEntity.ok(transactionService.findOverdueTransactions());
    }
}