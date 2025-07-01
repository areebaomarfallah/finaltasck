package com.library.library_management_system.service;

import com.library.library_management_system.dto.BookRequestDTO;
import com.library.library_management_system.dto.BookResponseDTO;
import com.library.library_management_system.emun.Category;
import com.library.library_management_system.model.Author;
import com.library.library_management_system.model.Book;
import com.library.library_management_system.repository.AuthorRepository;
import com.library.library_management_system.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private static final String BOOK_NOT_FOUND = "Book not found";
    private static final String AUTHOR_NOT_FOUND = "Author not found";

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    private Book toEntity(BookRequestDTO dto, Author author) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setCategory(dto.getCategory());
        book.setAvailable(dto.isAvailable());
        book.setAuthor(author);
        book.setBasePrice(dto.getBasePrice() != null ? dto.getBasePrice() : BigDecimal.ZERO);
        book.setExtraDaysRentalPrice(dto.getExtraDaysRentalPrice() != null ? dto.getExtraDaysRentalPrice() : BigDecimal.ZERO);
        book.setInsuranceFees(dto.getInsuranceFees() != null ? dto.getInsuranceFees() : BigDecimal.ZERO);
        return book;
    }
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND));
        return BookResponseDTO.fromEntity(book);
    }

    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        Author author = authorRepository.findById(bookRequestDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND));
        Book book = toEntity(bookRequestDTO, author);
        book.setAvailable(true); // default availability
        book = bookRepository.save(book);
        return BookResponseDTO.fromEntity(book);
    }

    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND));

        Author author = authorRepository.findById(bookRequestDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND));

        book.setTitle(bookRequestDTO.getTitle());
        book.setIsbn(bookRequestDTO.getIsbn());
        book.setCategory(bookRequestDTO.getCategory());
        book.setAvailable(bookRequestDTO.isAvailable());
        book.setAuthor(author);

        book = bookRepository.save(book);
        return BookResponseDTO.fromEntity(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<BookResponseDTO> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookResponseDTO> searchBooksByCategory(Category category) {
        return bookRepository.findByCategory(category).stream()
                .map(BookResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookResponseDTO> searchBooksByAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND));
        return bookRepository.findByAuthor(author).stream()
                .map(BookResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
