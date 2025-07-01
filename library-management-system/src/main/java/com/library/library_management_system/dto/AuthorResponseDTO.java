package com.library.library_management_system.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class AuthorResponseDTO {
    private Long id;
    private String name;
    private String biography;
    private List<Long> bookIds;

    public static AuthorResponseDTO fromEntity(com.library.library_management_system.model.Author author) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBiography(author.getBiography());
        dto.setBookIds(
                author.getBooks() != null && !author.getBooks().isEmpty() ?
                        author.getBooks().stream().map(book -> book.getId()).toList()
                        : Collections.emptyList()
        );
        return dto;
    }
}
