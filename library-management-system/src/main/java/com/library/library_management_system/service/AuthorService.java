package com.library.library_management_system.service;

import com.library.library_management_system.dto.AuthorRequestDTO;
import com.library.library_management_system.dto.AuthorResponseDTO;
import com.library.library_management_system.model.Author;
import com.library.library_management_system.model.Book;
import com.library.library_management_system.repository.AuthorRepository;
import com.library.library_management_system.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private static final String AUTHOR_NOT_FOUND = "Author not found";

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(AuthorResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private Author toEntity(AuthorRequestDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setBiography(dto.getBiography());

        if (dto.getBookIds() != null && !dto.getBookIds().isEmpty()) {
            List<Book> books = bookRepository.findAllById(dto.getBookIds());
            for (Book book : books) {
                book.setAuthor(author); // Set back reference
            }
            author.setBooks(books);
        }

        return author;
    }

    public AuthorResponseDTO getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(AuthorResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND));
    }

    public AuthorResponseDTO createAuthor(AuthorRequestDTO requestDTO) {
        Author author = toEntity(requestDTO);
        Author savedAuthor = authorRepository.save(author);
        return AuthorResponseDTO.fromEntity(savedAuthor);
    }

    public AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO requestDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND));

        existingAuthor.setName(requestDTO.getName());
        existingAuthor.setBiography(requestDTO.getBiography());

        if (requestDTO.getBookIds() != null) {
            // Optionally clear previous associations
            if (existingAuthor.getBooks() != null) {
                existingAuthor.getBooks().forEach(book -> book.setAuthor(null));
            }

            List<Book> updatedBooks = bookRepository.findAllById(requestDTO.getBookIds());
            for (Book book : updatedBooks) {
                book.setAuthor(existingAuthor);
            }
            existingAuthor.setBooks(updatedBooks);
        }

        Author updatedAuthor = authorRepository.save(existingAuthor);
        return AuthorResponseDTO.fromEntity(updatedAuthor);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
