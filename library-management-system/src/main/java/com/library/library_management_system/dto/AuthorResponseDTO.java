package com.library.library_management_system.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorResponseDTO {
    private UUID id;
    private String name;
    private String biography;
    private List<UUID> bookIds;


}
