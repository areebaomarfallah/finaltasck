package com.library.library_management_system.controller;

import com.library.library_management_system.dto.AuthorRequestDTO;
import com.library.library_management_system.dto.AuthorResponseDTO;
import com.library.library_management_system.dto.BookResponseDTO;
import com.library.library_management_system.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable UUID id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.createAuthor(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(
            @PathVariable UUID id,
            @Valid @RequestBody AuthorRequestDTO requestDTO) {
        return ResponseEntity.ok(authorService.updateAuthor(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/books")
    public ResponseEntity<List<UUID>> getBooksByAuthor(@PathVariable UUID id) {
        return ResponseEntity.ok(authorService.getBooksByAuthor(id));
    }

    @PutMapping("/{authorId}/books/{bookId}/add")
    public ResponseEntity<Void> addBookToAuthor(@PathVariable UUID authorId, @PathVariable UUID bookId) {
        authorService.addBookToAuthor(authorId, bookId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{authorId}/books/{bookId}/remove")
    public ResponseEntity<Void> removeBookFromAuthor(@PathVariable UUID authorId, @PathVariable UUID bookId) {
        authorService.removeBookFromAuthor(authorId, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getAuthorName(@PathVariable UUID id) {
        return ResponseEntity.ok(authorService.getAuthorName(id));
    }

}