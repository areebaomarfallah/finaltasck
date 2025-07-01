package com.library.library_management_system.dto;

import com.library.library_management_system.emun.Category;
import com.library.library_management_system.model.Book;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private Category category;
    private boolean available;
    private Long authorId;
    private String authorName;
    private BigDecimal basePrice;
    private BigDecimal extraDaysRentalPrice;
    private BigDecimal insuranceFees;

    public static BookResponseDTO fromEntity(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setCategory(book.getCategory());
        dto.setAvailable(book.isAvailable());
        dto.setBasePrice(book.getBasePrice());
        dto.setExtraDaysRentalPrice(book.getExtraDaysRentalPrice());
        dto.setInsuranceFees(book.getInsuranceFees());

        if (book.getAuthor() != null) {
            dto.setAuthorId(book.getAuthor().getId());
            dto.setAuthorName(book.getAuthor().getName());
        }
        return dto;
    }
}