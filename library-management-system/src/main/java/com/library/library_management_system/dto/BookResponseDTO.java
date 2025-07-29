package com.library.library_management_system.dto;

import com.library.library_management_system.utils.CommonEnum;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class BookResponseDTO {
    private UUID id;
    private String title;
    private String isbn;
    private CommonEnum.Category category;
    private boolean available;
    private UUID authorId;
    private String authorName;
    private BigDecimal basePrice;
    private BigDecimal extraDaysRentalPrice;
    private BigDecimal insuranceFees;
}