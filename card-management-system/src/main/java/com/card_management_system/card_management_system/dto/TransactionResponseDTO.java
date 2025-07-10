package com.card_management_system.card_management_system.dto;

import com.card_management_system.card_management_system.utils.CommonEnum;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TransactionResponseDTO {
    private UUID id;

    private BigDecimal transactionAmount;

    private LocalDateTime transactionDate;

    private CommonEnum.TransactionType transactionType;

    private CommonEnum.Status status;

    private String message;

    private UUID cardId;
}
