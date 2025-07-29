package com.library.library_management_system.controller;

import com.library.library_management_system.dto.BookRequestDTO;
import com.library.library_management_system.dto.BookResponseDTO;
import com.library.library_management_system.utils.CommonEnum;
import com.library.library_management_system.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable UUID id,
            @Valid @RequestBody BookRequestDTO requestDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookResponseDTO>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<BookResponseDTO>> searchByCategory(@RequestParam CommonEnum.Category category) {
        return ResponseEntity.ok(bookService.searchBooksByCategory(category));
    }

    @GetMapping("/search/author/{authorId}")
    public ResponseEntity<List<BookResponseDTO>> searchByAuthor(@PathVariable UUID authorId) {
        return ResponseEntity.ok(bookService.searchBooksByAuthor(authorId));
    }
}