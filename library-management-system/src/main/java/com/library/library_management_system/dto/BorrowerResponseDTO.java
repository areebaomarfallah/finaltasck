package com.library.library_management_system.dto;

import com.library.library_management_system.emun.AccountStatus;
import com.library.library_management_system.model.Borrower;
import lombok.Data;

@Data
public class BorrowerResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private AccountStatus status;

    public static BorrowerResponseDTO fromEntity(Borrower borrower) {
        BorrowerResponseDTO dto = new BorrowerResponseDTO();
        dto.setId(borrower.getId());
        dto.setName(borrower.getName());
        dto.setEmail(borrower.getEmail());
        dto.setPhoneNumber(borrower.getPhoneNumber());
        dto.setStatus(borrower.getStatus());
        return dto;
    }
}
