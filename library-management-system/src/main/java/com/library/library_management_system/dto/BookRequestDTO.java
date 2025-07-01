package com.library.library_management_system.dto;

import com.library.library_management_system.emun.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequestDTO {
    private String title;
    private String isbn;
    private Category category;
    private boolean available;
    private Long authorId;
    private BigDecimal basePrice;
    private BigDecimal extraDaysRentalPrice;
    private BigDecimal insuranceFees;
}