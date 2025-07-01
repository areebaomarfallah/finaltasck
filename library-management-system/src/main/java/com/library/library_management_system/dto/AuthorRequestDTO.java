package com.library.library_management_system.dto;

import lombok.Data;
import java.util.List;

@Data
public class AuthorRequestDTO {
    private String name;
    private String biography;
    private List<Long> bookIds; // IDs of books to associate with the author
}
