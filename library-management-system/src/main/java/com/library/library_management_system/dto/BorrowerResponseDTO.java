// BorrowerResponseDTO.java
package com.library.library_management_system.dto;

import com.library.library_management_system.utils.CommonEnum;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class BorrowerResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private CommonEnum.AccountStatus status;
}