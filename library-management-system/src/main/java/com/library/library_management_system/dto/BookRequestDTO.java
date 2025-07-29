// BookRequestDTO.java
package com.library.library_management_system.dto;

import com.library.library_management_system.utils.CommonEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class BookRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "ISBN is required")
    private String isbn;
    
    @NotNull(message = "Category is required")
    private CommonEnum.Category category;
    
    @NotNull(message = "Availability status is required")
    private boolean available;
    
    @NotNull(message = "Author ID is required")
    private UUID authorId;
    
    @NotNull(message = "Base price is required")
    private BigDecimal basePrice;
    
    @NotNull(message = "Extra days rental price is required")
    private BigDecimal extraDaysRentalPrice;
    
    @NotNull(message = "Insurance fees are required")
    private BigDecimal insuranceFees;
}