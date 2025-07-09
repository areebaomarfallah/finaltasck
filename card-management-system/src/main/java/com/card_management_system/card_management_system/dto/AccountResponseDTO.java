package com.card_management_system.card_management_system.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.card_management_system.card_management_system.utils.CommonEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponseDTO {

    private UUID id;

    private CommonEnum.StatusType status;

    private BigDecimal balance;
}
