package com.library.library_management_system.repository;

import com.library.library_management_system.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    // Add these methods
    boolean existsByEmail(String email);

    Optional<Borrower> findByEmail(String email);

    // Keep any existing methods
}