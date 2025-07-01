package com.library.library_management_system.controler;

import com.library.library_management_system.dto.BorrowerRequestDTO;
import com.library.library_management_system.dto.BorrowerResponseDTO;
import com.library.library_management_system.service.BorrowerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @GetMapping
    public ResponseEntity<List<BorrowerResponseDTO>> getAllBorrowers() {
        return ResponseEntity.ok(borrowerService.getAllBorrowers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponseDTO> getBorrowerById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowerService.getBorrowerById(id));
    }

    @PostMapping
    public ResponseEntity<BorrowerResponseDTO> createBorrower(@RequestBody BorrowerRequestDTO borrowerRequestDTO) {
        BorrowerResponseDTO created = borrowerService.createBorrower(borrowerRequestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowerResponseDTO> updateBorrower(@PathVariable Long id, @RequestBody BorrowerRequestDTO borrowerRequestDTO) {
        BorrowerResponseDTO updated = borrowerService.updateBorrower(id, borrowerRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/borrowemail")
    public ResponseEntity<String> borrowBook(
            @PathVariable Long id,
            @RequestParam String bookTitle
    ) {
        borrowerService.borrowBook(id, bookTitle);
        return ResponseEntity.ok("Book borrowed and email sent successfully.");
    }
}