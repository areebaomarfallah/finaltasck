package com.library.library_management_system.service;

import com.library.library_management_system.dto.BookRequestDTO;
import com.library.library_management_system.dto.BookResponseDTO;
import com.library.library_management_system.dto.converter.BookConverter;
import com.library.library_management_system.exception.BusinessRuleException;
import com.library.library_management_system.exception.ResourceNotFoundException;
import com.library.library_management_system.model.Book;
import com.library.library_management_system.repository.BookRepository;
import com.library.library_management_system.utils.CommonEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final BookConverter bookConverter;

    public BookResponseDTO createBook(BookRequestDTO requestDTO) {
        authorService.getAuthorById(requestDTO.getAuthorId());

        Book book = bookConverter.toEntity(requestDTO);
        Book savedBook = bookRepository.save(book);

        authorService.addBookToAuthor(savedBook.getAuthorId(), savedBook.getId());

        return bookConverter.toDto(savedBook);
    }

    public BookResponseDTO updateBook(UUID id, BookRequestDTO requestDTO) {
        Book existingBook = getBookEntity(id);

        if (!existingBook.getAuthorId().equals(requestDTO.getAuthorId())) {
            authorService.removeBookFromAuthor(existingBook.getAuthorId(), id);

            authorService.addBookToAuthor(requestDTO.getAuthorId(), id);
        }

        Book updatedBook = bookConverter.toEntity(requestDTO);
        updatedBook.setId(id);
        return bookConverter.toDto(bookRepository.save(updatedBook));
    }

    public List<BookResponseDTO> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(bookConverter::toDto)
                .toList();
    }

    public List<BookResponseDTO> searchBooksByCategory(CommonEnum.Category category) {
        return bookRepository.findByCategory(category).stream()
                .map(bookConverter::toDto)
                .toList();
    }

    public List<BookResponseDTO> searchBooksByAuthor(UUID authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
                .map(bookConverter::toDto)
                .toList();
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookConverter::toDto)
                .toList();
    }




    public void deleteBook(UUID bookId) {
        Book book = getBookEntity(bookId);

        authorService.removeBookFromAuthor(book.getAuthorId(), bookId);

        bookRepository.deleteById(bookId);
    }

    public void deleteBooksByAuthor(UUID authorId) {
        List<Book> books = bookRepository.findByAuthorId(authorId);
        books.forEach(book -> bookRepository.deleteById(book.getId()));
    }

    public String getBookTitle(UUID bookId) {
        return bookRepository.findById(bookId)
                .map(Book::getTitle)
                .orElse("Unknown Book");
    }



    @Transactional
    public void markBookAsAvailable(UUID bookId) {
        Book book = getBookEntity(bookId);
        book.setAvailable(true);
        bookRepository.save(book);
    }
    public BookResponseDTO getBookById(UUID id) {
        Book book = getBookEntity(id);
        BookResponseDTO dto = bookConverter.toDto(book);
        dto.setAuthorName(authorService.getAuthorName(book.getAuthorId()));
        return dto;
    }

    // Validation methods
    public void validateBookExists(UUID bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book", bookId);
        }
    }

    public void validateBookAvailable(UUID bookId) {
        if (!getBookEntity(bookId).isAvailableForBorrow()) {
            throw new BusinessRuleException("Book is not available for borrowing");
        }
    }


    @Transactional
    public void markBookAsBorrowed(UUID bookId) {
        Book book = getBookEntity(bookId);
        if (!book.isAvailable()) {
            throw new BusinessRuleException("Book is already borrowed");
        }
        book.setAvailable(false);
        bookRepository.save(book);
    }


    public Book getBookEntity(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }
}