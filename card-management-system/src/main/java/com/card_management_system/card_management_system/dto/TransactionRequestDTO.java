package com.card_management_system.card_management_system.dto;

import com.card_management_system.card_management_system.utils.CommonEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequestDTO {
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal transactionAmount;

    @NotNull(message = "Transaction type is required")
    private CommonEnum.TransactionType transactionType;

    @NotBlank(message = "Card number is required")
    private String cardNumberHash;
}