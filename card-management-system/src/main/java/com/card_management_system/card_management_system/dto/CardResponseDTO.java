package com.card_management_system.card_management_system.dto;

import com.card_management_system.card_management_system.utils.CommonEnum;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CardResponseDTO {

    private UUID id;

    private CommonEnum.StatusType status;

    private LocalDate expiry;

    private String cardNumber;

    private UUID accountId;




}
