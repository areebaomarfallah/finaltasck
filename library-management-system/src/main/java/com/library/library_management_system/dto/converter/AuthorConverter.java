package com.library.library_management_system.dto.converter;

import com.library.library_management_system.dto.AuthorRequestDTO;
import com.library.library_management_system.dto.AuthorResponseDTO;
import com.library.library_management_system.model.Author;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthorConverter {
    private final ModelMapper modelMapper;

    public AuthorResponseDTO toDto(Author author) {

        return modelMapper.map(author, AuthorResponseDTO.class);
    }

    public Author toEntity(AuthorRequestDTO dto) {

        return modelMapper.map(dto, Author.class);
    }



}
