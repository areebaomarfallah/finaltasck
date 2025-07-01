package com.library.library_management_system.repository;

import com.library.library_management_system.emun.TransactionStatus;
import com.library.library_management_system.model.Book;
import com.library.library_management_system.model.Borrower;
import com.library.library_management_system.model.BorrowingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {

    // Find all transactions by borrower
    List<BorrowingTransaction> findByBorrower(Borrower borrower);

    // Find all transactions by book
    List<BorrowingTransaction> findByBook(Book book);

    // Find current (unreturned) transaction for a book
    Optional<BorrowingTransaction> findFirstByBookAndStatus(Book book, TransactionStatus status);

    // Check if borrower has active transactions (NEW)
    boolean existsByBorrowerAndStatus(Borrower borrower, TransactionStatus status);

    // Find transactions by status
    List<BorrowingTransaction> findByStatus(TransactionStatus status);

    // Find overdue transactions (where due date is before current date and status is BORROWED)
    List<BorrowingTransaction> findByDueDateBeforeAndStatus(LocalDateTime date, TransactionStatus status);
}