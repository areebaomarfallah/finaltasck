package com.library.library_management_system.repository;

import com.library.library_management_system.model.Book;
import com.library.library_management_system.utils.CommonEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByCategory(CommonEnum.Category category);

    List<Book> findByAuthorId(UUID authorId);


}