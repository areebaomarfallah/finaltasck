package com.library.library_management_system.service;

import com.library.library_management_system.dto.BookRequestDTO;
import com.library.library_management_system.dto.BookResponseDTO;
import com.library.library_management_system.dto.converter.BookConverter;
import com.library.library_management_system.exception.BusinessRuleException;
import com.library.library_management_system.exception.ResourceNotFoundException;
import com.library.library_management_system.model.Author;
import com.library.library_management_system.model.Book;
import com.library.library_management_system.repository.AuthorRepository;
import com.library.library_management_system.repository.BookRepository;
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
    private final AuthorRepository authorRepository;
    private final BookConverter bookConverter;

    public BookResponseDTO createBook(BookRequestDTO requestDTO) {
        Author author = getAuthorEntity(requestDTO.getAuthorId());
        Book book = bookConverter.toEntity(requestDTO);
        Book savedBook = bookRepository.save(book);

        author.getBookIds().add(savedBook.getId());
        authorRepository.save(author);

        return bookConverter.toDto(savedBook);
    }

    public BookResponseDTO updateBook(UUID id, BookRequestDTO requestDTO) {
        Book existingBook = getBookEntity(id);

        UUID oldAuthorId = existingBook.getAuthorId();
        UUID newAuthorId = requestDTO.getAuthorId();

        if (!oldAuthorId.equals(newAuthorId)) {
            Author oldAuthor = getAuthorEntity(oldAuthorId);
            oldAuthor.getBookIds().remove(id);
            authorRepository.save(oldAuthor);

            Author newAuthor = getAuthorEntity(newAuthorId);
            newAuthor.getBookIds().add(id);
            authorRepository.save(newAuthor);
        }

        Book updatedBook = bookConverter.toEntity(requestDTO);
        updatedBook.setId(id);
        return bookConverter.toDto(bookRepository.save(updatedBook));
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookConverter::toDto)
                .toList();
    }

    public BookResponseDTO getBookById(UUID id) {
        Book book = getBookEntity(id);
        BookResponseDTO dto = bookConverter.toDto(book);
        dto.setAuthorName(getAuthorEntity(book.getAuthorId()).getName());
        return dto;
    }

    public void deleteBook(UUID bookId) {
        Book book = getBookEntity(bookId);
        Author author = getAuthorEntity(book.getAuthorId());
        author.getBookIds().remove(bookId);
        authorRepository.save(author);
        bookRepository.deleteById(bookId);
    }

    public void deleteBooksByAuthor(UUID authorId) {
        List<Book> books = bookRepository.findByAuthorId(authorId);
        books.forEach(book -> deleteBook(book.getId()));
    }

    public void markBookAsAvailable(UUID bookId) {
        Book book = getBookEntity(bookId);
        book.setAvailable(true);
        bookRepository.save(book);
    }

    public void markBookAsBorrowed(UUID bookId) {
        Book book = getBookEntity(bookId);
        if (!book.isAvailable()) {
            throw new BusinessRuleException("Book is already borrowed");
        }
        book.setAvailable(false);
        bookRepository.save(book);
    }

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

    public String getBookTitle(UUID bookId) {
        return getBookEntity(bookId).getTitle();
    }

    public Book getBookEntity(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    private Author getAuthorEntity(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }
}
