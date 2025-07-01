package com.library.library_management_system.repository;

import com.library.library_management_system.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Optional: Search authors by name
    List<Author> findByNameContainingIgnoreCase(String name);
}
