package com.library.library_management_system.service;

import com.library.library_management_system.client.EmailClient;
import com.library.library_management_system.dto.*;
import com.library.library_management_system.emun.AccountStatus;
import com.library.library_management_system.emun.HashUtil;
import com.library.library_management_system.emun.TransactionStatus;
import com.library.library_management_system.exception.*;
import com.library.library_management_system.model.*;
import com.library.library_management_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowerService {

    private static final Logger LOGGER = Logger.getLogger(BorrowerService.class.getName());

    private final BorrowerRepository borrowerRepository;
    private final EmailClient emailClient;
    private final BorrowingTransactionRepository transactionRepository;

    @Autowired
    public BorrowerService(BorrowerRepository borrowerRepository,
                           EmailClient emailClient,
                           BorrowingTransactionRepository transactionRepository) {
        this.borrowerRepository = borrowerRepository;
        this.emailClient = emailClient;
        this.transactionRepository = transactionRepository;
    }

    public List<BorrowerResponseDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(BorrowerResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public BorrowerResponseDTO getBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .map(BorrowerResponseDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + id));
    }

    public BorrowerResponseDTO createBorrower(BorrowerRequestDTO dto) {
        validateBorrowerData(dto);

        if (borrowerRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("Email already registered");
        }

        Borrower borrower = new Borrower();
        borrower.setName(dto.getName());
        borrower.setEmail(dto.getEmail());
        borrower.setPhoneNumber(dto.getPhoneNumber());
        borrower.setStatus(dto.getStatus() != null ? dto.getStatus() : AccountStatus.ACTIVE);
        borrower.setCardNumberHash(HashUtil.hashCardNumber(dto.getCardNumber()));

        Borrower saved = borrowerRepository.save(borrower);
        sendWelcomeEmail(saved);
        return BorrowerResponseDTO.fromEntity(saved);
    }

    public BorrowerResponseDTO updateBorrower(Long id, BorrowerRequestDTO dto) {
        validateBorrowerData(dto);

        Borrower existing = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + id));

        if (!existing.getEmail().equals(dto.getEmail()) &&
                borrowerRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("Email already in use by another borrower");
        }

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setStatus(dto.getStatus());

        if (dto.getCardNumber() != null && !dto.getCardNumber().isEmpty()) {
            existing.setCardNumberHash(HashUtil.hashCardNumber(dto.getCardNumber()));
        }

        return BorrowerResponseDTO.fromEntity(borrowerRepository.save(existing));
    }

    public void deleteBorrower(Long id) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + id));

        if (transactionRepository.existsByBorrowerAndStatus(borrower, TransactionStatus.BORROWED)) {
            throw new BusinessRuleException("Cannot delete borrower with active book loans");
        }

        borrowerRepository.delete(borrower);
    }

    public void updateBorrowerStatus(Long id, BorrowerStatusUpdateDTO dto) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + id));

        if (!borrower.getStatus().canTransitionTo(dto.getStatus())) {
            throw new InvalidStatusTransitionException(
                    String.format("Cannot change status from %s to %s",
                            borrower.getStatus(), dto.getStatus())
            );
        }

        borrower.setStatus(dto.getStatus());
        borrowerRepository.save(borrower);
        sendStatusChangeNotification(borrower);
    }

    public void borrowBook(Long borrowerId, String bookTitle) {
        borrowBook(borrowerId, bookTitle, 7); // Default 7 days duration
    }

    public void borrowBook(Long borrowerId, String bookTitle, int durationDays) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));

        if (borrower.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessRuleException("Borrower account is not active");
        }

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(borrower.getEmail());
        emailRequest.setMessage(String.format(
                "You have successfully borrowed: %s\nDuration: %d days\nDue date: %s",
                bookTitle,
                durationDays,
                LocalDateTime.now().plusDays(durationDays).toLocalDate()
        ));

        try {
            emailClient.sendEmail(emailRequest);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to send borrow confirmation to " + borrower.getEmail(), e);
            throw new EmailNotificationException("Failed to send borrow confirmation");
        }
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

    private void sendWelcomeEmail(Borrower borrower) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(borrower.getEmail());
        emailRequest.setMessage(String.format(
                "Welcome to our library, %s!\n\nYour account details:\nEmail: %s\nStatus: %s",
                borrower.getName(),
                borrower.getEmail(),
                borrower.getStatus()
        ));

        try {
            emailClient.sendEmail(emailRequest);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to send welcome email to " + borrower.getEmail(), e);
        }
    }

    private void sendStatusChangeNotification(Borrower borrower) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(borrower.getEmail());
        emailRequest.setMessage(String.format(
                "Account Status Update\n\nDear %s,\nYour account status has been changed to: %s",
                borrower.getName(),
                borrower.getStatus()
        ));

        try {
            emailClient.sendEmail(emailRequest);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to send status notification to " + borrower.getEmail(), e);
        }
    }
}