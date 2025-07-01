package com.library.library_management_system.repository;

import com.library.library_management_system.emun.Category;
import com.library.library_management_system.model.Author;
import com.library.library_management_system.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Search books by title (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Search books by category enum
    List<Book> findByCategory(Category category);

    // Search books by author
    List<Book> findByAuthor(Author author);

    // Search available books
    List<Book> findByAvailable(boolean available);
}
