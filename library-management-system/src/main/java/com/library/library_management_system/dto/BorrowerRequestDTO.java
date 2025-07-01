package com.library.library_management_system.dto;

import com.library.library_management_system.emun.AccountStatus;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class BorrowerRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private AccountStatus status;

    @Pattern(regexp = "^[0-9]{13,19}$", message = "Card number must be 13-19 digits")
    private String cardNumber;
}