package com.library.library_management_system.dto.converter;

import com.library.library_management_system.dto.BorrowingTransactionRequestDTO;
import com.library.library_management_system.dto.BorrowingTransactionResponseDTO;
import com.library.library_management_system.model.BorrowingTransaction;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BorrowingTransactionConverter {
    private final ModelMapper modelMapper;

    public BorrowingTransactionResponseDTO toDto(BorrowingTransaction transaction) {
        return modelMapper.map(transaction, BorrowingTransactionResponseDTO.class);
    }

    public BorrowingTransaction toEntity(BorrowingTransactionRequestDTO dto) {
        return modelMapper.map(dto, BorrowingTransaction.class);
    }
}