package com.library.library_management_system.dto.converter;

import com.library.library_management_system.dto.BorrowerRequestDTO;
import com.library.library_management_system.dto.BorrowerResponseDTO;
import com.library.library_management_system.model.Borrower;
import com.library.library_management_system.utils.CardUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BorrowerConverter {
    private final ModelMapper modelMapper;
    private final CardUtils cardUtils;

    public BorrowerResponseDTO toDto(Borrower borrower) {
        return   modelMapper.map(borrower, BorrowerResponseDTO.class);

    }

    public Borrower toEntity(BorrowerRequestDTO dto) {
        Borrower borrower = modelMapper.map(dto, Borrower.class);
        if (dto.getCardNumber() != null && !dto.getCardNumber().isEmpty()) {
            borrower.setCardNumberHash(cardUtils.hashCardNumber(dto.getCardNumber()));
        }
        return borrower;
    }
}