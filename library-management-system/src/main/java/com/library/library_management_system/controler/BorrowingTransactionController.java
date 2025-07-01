package com.library.library_management_system.controler;

import com.library.library_management_system.dto.BorrowingTransactionRequestDTO;
import com.library.library_management_system.dto.BorrowingTransactionResponseDTO;
import com.library.library_management_system.service.BorrowingTransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class BorrowingTransactionController {

    private final BorrowingTransactionService transactionService;

    public BorrowingTransactionController(BorrowingTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingTransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping
    public ResponseEntity<BorrowingTransactionResponseDTO> createTransaction(
            @Valid @RequestBody BorrowingTransactionRequestDTO requestDTO) {
        BorrowingTransactionResponseDTO created = transactionService.createTransaction(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingTransactionResponseDTO> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long borrowerId,
            @RequestParam(defaultValue = "7") @Min(1) int durationDays) {
        BorrowingTransactionResponseDTO tx = transactionService.borrowBook(bookId, borrowerId, durationDays);
        return ResponseEntity.status(HttpStatus.CREATED).body(tx);
    }

    @PostMapping("/return/{transactionId}")
    public ResponseEntity<BorrowingTransactionResponseDTO> returnBook(@PathVariable Long transactionId) {
        BorrowingTransactionResponseDTO tx = transactionService.returnBook(transactionId);
        return ResponseEntity.ok(tx);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getByBorrower(@PathVariable Long borrowerId) {
        return ResponseEntity.ok(transactionService.findByBorrower(borrowerId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(transactionService.findByBook(bookId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getOverdueTransactions() {
        return ResponseEntity.ok(transactionService.findOverdueTransactions());
    }
}