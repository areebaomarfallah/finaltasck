package com.library.library_management_system.service;

import com.library.library_management_system.dto.AuthorRequestDTO;
import com.library.library_management_system.dto.AuthorResponseDTO;
import com.library.library_management_system.dto.converter.AuthorConverter;
import com.library.library_management_system.exception.ResourceNotFoundException;
import com.library.library_management_system.model.Author;
import com.library.library_management_system.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorConverter authorConverter;
    private final BookService bookService;

    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorConverter::toDto)
                .toList();
    }

    public AuthorResponseDTO getAuthorById(UUID id) {
        return authorConverter.toDto(getAuthorEntity(id));
    }

    public AuthorResponseDTO createAuthor(AuthorRequestDTO requestDTO) {
        Author author = authorConverter.toEntity(requestDTO);
        return authorConverter.toDto(authorRepository.save(author));
    }

    public AuthorResponseDTO updateAuthor(UUID id, AuthorRequestDTO requestDTO) {
        Author author = getAuthorEntity(id);
        author.setName(requestDTO.getName());
        author.setBiography(requestDTO.getBiography());
        return authorConverter.toDto(authorRepository.save(author));
    }

    public void deleteAuthor(UUID id) {
        bookService.deleteBooksByAuthor(id);
        authorRepository.deleteById(id);
    }

    public List<UUID> getBooksByAuthor(UUID id) {
        return getAuthorEntity(id).getBookIds();
    }

    public void removeBookFromAuthor(UUID authorId, UUID bookId) {
        Author author = getAuthorEntity(authorId);
        author.getBookIds().remove(bookId);
        authorRepository.save(author);
    }

    public void addBookToAuthor(UUID authorId, UUID bookId) {
        Author author = getAuthorEntity(authorId);
        bookService.validateBookExists(bookId);
        author.getBookIds().add(bookId);
        authorRepository.save(author);
    }

    public String getAuthorName(UUID authorId) {
        return getAuthorEntity(authorId).getName();
    }

    private Author getAuthorEntity(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }
}