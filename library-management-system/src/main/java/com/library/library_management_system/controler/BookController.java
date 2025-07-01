package com.library.library_management_system.controler;

import com.library.library_management_system.dto.BookRequestDTO;
import com.library.library_management_system.dto.BookResponseDTO;
import com.library.library_management_system.emun.Category;
import com.library.library_management_system.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO created = bookService.createBook(bookRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO updated = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookResponseDTO>> searchByTitle(@RequestParam String title) {
        List<BookResponseDTO> results = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<BookResponseDTO>> searchByCategory(@RequestParam Category category) {
        List<BookResponseDTO> results = bookService.searchBooksByCategory(category);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/author/{authorId}")
    public ResponseEntity<List<BookResponseDTO>> searchByAuthor(@PathVariable Long authorId) {
        List<BookResponseDTO> results = bookService.searchBooksByAuthor(authorId);
        return ResponseEntity.ok(results);
    }
}
