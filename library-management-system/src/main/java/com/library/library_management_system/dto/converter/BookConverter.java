package com.library.library_management_system.dto.converter;

import com.library.library_management_system.dto.BookRequestDTO;
import com.library.library_management_system.dto.BookResponseDTO;
import com.library.library_management_system.model.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookConverter {
    private final ModelMapper modelMapper;

    public BookResponseDTO toDto(Book book) {
        BookResponseDTO dto = modelMapper.map(book, BookResponseDTO.class);
        dto.setAuthorId(book.getAuthorId());
        return dto;
    }

    public Book toEntity(BookRequestDTO dto) {
        Book book = modelMapper.map(dto, Book.class);
        book.setAuthorId(dto.getAuthorId());
        return book;
    }
}