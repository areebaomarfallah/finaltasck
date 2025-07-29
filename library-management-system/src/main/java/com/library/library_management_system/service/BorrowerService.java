package com.library.library_management_system.service;

import com.library.library_management_system.dto.*;
import com.library.library_management_system.dto.converter.BorrowerConverter;
import com.library.library_management_system.exception.*;
import com.library.library_management_system.model.Borrower;
import com.library.library_management_system.repository.BorrowerRepository;
import com.library.library_management_system.utils.CommonEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;
    private final BorrowerConverter borrowerConverter;

    public List<BorrowerResponseDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(borrowerConverter::toDto)
                .toList();
    }

    public BorrowerResponseDTO getBorrowerById(UUID id) {
        return borrowerConverter.toDto(getBorrowerEntity(id));
    }

    public BorrowerResponseDTO createBorrower(BorrowerRequestDTO dto) {
        validateBorrowerData(dto);
        if (borrowerRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("Email already registered");
        }

        Borrower borrower = borrowerConverter.toEntity(dto);
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return borrowerConverter.toDto(savedBorrower);
    }

    public void deleteBorrower(UUID id) {
        Borrower borrower = getBorrowerEntity(id);
        if (!borrower.getTransactionIds().isEmpty()) {
            throw new BusinessRuleException("Cannot delete borrower with associated transactions");
        }
        borrowerRepository.delete(borrower);
    }

    public BorrowerResponseDTO updateBorrower(UUID id, BorrowerRequestDTO dto) {
        validateBorrowerData(dto);
        Borrower existing = getBorrowerEntity(id);

        if (!existing.getEmail().equals(dto.getEmail()) &&
                borrowerRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("Email already in use by another borrower");
        }

        Borrower updated = borrowerConverter.toEntity(dto);
        updated.setId(id);
        updated.setTransactionIds(existing.getTransactionIds());
        return borrowerConverter.toDto(borrowerRepository.save(updated));
    }



    public void updateBorrowerStatus(UUID id, CommonEnum.AccountStatus newStatus) {
        Borrower borrower = getBorrowerEntity(id);
        if (!borrower.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(
                    String.format("Cannot change status from %s to %s", borrower.getStatus(), newStatus));
        }
        borrower.setStatus(newStatus);
        borrowerRepository.save(borrower);
    }


    private void validateBorrowerData(BorrowerRequestDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (dto.getEmail() == null || !dto.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().matches("^\\+?[0-9\\-\\s]{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        if (dto.getCardNumber() != null && !dto.getCardNumber().matches("^[0-9]{13,19}$")) {
            throw new IllegalArgumentException("Card number must be 13-19 digits");
        }
    }
    public String getBorrowerName(UUID borrowerId) {
        return borrowerRepository.findById(borrowerId)
                .map(Borrower::getName)
                .orElse("Unknown Borrower");
    }

    @Transactional
    public void removeTransactionFromBorrower(UUID borrowerId, UUID transactionId) {
        Borrower borrower = getBorrowerEntity(borrowerId);
        borrower.getTransactionIds().remove(transactionId);
        borrowerRepository.save(borrower);
    }



    public void validateBorrowerActive(UUID borrowerId) {
        if (getBorrowerEntity(borrowerId).getStatus() != CommonEnum.AccountStatus.ACTIVE) {
            throw new BusinessRuleException("Borrower account is not active");
        }
    }

    @Transactional
    public void addTransactionToBorrower(UUID borrowerId, UUID transactionId) {
        Borrower borrower = getBorrowerEntity(borrowerId);
        borrower.getTransactionIds().add(transactionId);
        borrowerRepository.save(borrower);
    }

    public Borrower getBorrowerEntity(UUID id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower", id));
    }
}