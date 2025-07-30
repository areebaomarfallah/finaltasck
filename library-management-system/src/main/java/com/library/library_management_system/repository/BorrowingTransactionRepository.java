package com.library.library_management_system.repository;

import com.library.library_management_system.model.BorrowingTransaction;
import com.library.library_management_system.utils.CommonEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, UUID> {

    List<BorrowingTransaction> findByBookId(UUID bookId);
    List<BorrowingTransaction> findByBorrowerId(UUID borrowerId);
    List<BorrowingTransaction> findByDueDateBeforeAndStatus(LocalDateTime date, CommonEnum.TransactionStatus status);
}