
package com.library.library_management_system.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "author_id")
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;


    @ElementCollection
    @CollectionTable(name = "author_books", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "book_id")
    private List<UUID> bookIds = new ArrayList<>();
}