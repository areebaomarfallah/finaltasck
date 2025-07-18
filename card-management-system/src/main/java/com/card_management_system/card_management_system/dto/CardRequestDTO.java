package com.card_management_system.card_management_system.dto;

import com.card_management_system.card_management_system.utils.CommonEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CardRequestDTO {
    @NotNull(message = "Status is required")
    private CommonEnum.StatusType status;

    @NotNull(message = "Account ID is required")
    private UUID accountId;
}