
package com.library.library_management_system.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;


@Getter
@Setter
public class AuthorRequestDTO {
    @NotBlank(message = "Author name is required")
    private String name;
    @NotBlank(message = "Author biography is required")
    private String biography;
}