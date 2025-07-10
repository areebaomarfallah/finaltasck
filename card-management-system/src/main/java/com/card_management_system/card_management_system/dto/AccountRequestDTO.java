package com.card_management_system.card_management_system.dto;

import com.card_management_system.card_management_system.utils.CommonEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequestDTO {
    @NotNull(message = "Status is required")
    private CommonEnum.StatusType status;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    private BigDecimal balance;
}