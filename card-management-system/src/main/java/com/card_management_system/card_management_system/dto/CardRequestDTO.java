package com.card_management_system.card_management_system.dto;

import com.card_management_system.card_management_system.utils.CommonEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CardRequestDTO {
    @NotNull(message = "Status is required")
    private CommonEnum.StatusType status;


    @NotNull(message = "Expiry date is required")
    private LocalDate expiry;

    @NotNull(message = "Card number is required")
    @Size(min = 13, max = 19, message = "Card number must be between 13 and 19 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
    private String cardNumber;

    @NotNull(message = "Account ID is required")
    private UUID accountId;
}