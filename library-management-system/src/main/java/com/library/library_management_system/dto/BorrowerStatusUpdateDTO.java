package com.library.library_management_system.dto;

import com.library.library_management_system.emun.AccountStatus;
import lombok.Data;

@Data
public class BorrowerStatusUpdateDTO {
    private AccountStatus status;
}
