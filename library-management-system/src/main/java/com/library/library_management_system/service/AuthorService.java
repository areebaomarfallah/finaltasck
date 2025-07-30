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
    private final  BookService bookService;
    public List<AuthorResponseDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(authorConverter::toDto)
                .toList();
    }

    public AuthorResponseDTO getAuthorById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return authorConverter.toDto(author);
    }
    public AuthorResponseDTO createAuthor(AuthorRequestDTO requestDTO) {
        Author author = authorConverter.toEntity(requestDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorConverter.toDto(savedAuthor);
    }

    public AuthorResponseDTO updateAuthor(UUID id, AuthorRequestDTO requestDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));


            existingAuthor.setName(requestDTO.getName());


            existingAuthor.setBiography(requestDTO.getBiography());


        Author updatedAuthor = authorRepository.save(existingAuthor);
        return authorConverter.toDto(updatedAuthor);
    }

    public void deleteAuthor(UUID id) {

        bookService.deleteBooksByAuthor(id);

        authorRepository.deleteById(id);
    }

    public List<UUID> getBooksByAuthor(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return author.getBookIds();
    }


        @Transactional
        public void removeBookFromAuthor(UUID authorId, UUID bookId) {
            Author author = getAuthorEntity(authorId);
            author.getBookIds().remove(bookId);
            authorRepository.save(author);
        }

    @Transactional
    public void addBookToAuthor(UUID authorId, UUID bookId) {
        Author author = getAuthorEntity(authorId);
        bookService.validateBookExists(bookId);
        author.getBookIds().add(bookId);
        authorRepository.save(author);
    }

    private Author getAuthorEntity(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }

    public String getAuthorName(UUID authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", authorId));
        return authorConverter.toDto(author).getName();
    }
}