package fzs.lab.jpa.projection.dto;

import fzs.lab.jpa.projection.domain.Role;

import java.time.LocalDateTime;
import java.util.Set;

public interface UserDto {
    // Add other fields as needed from the User entity
    // No need for setters as it's a jpa projection
    String getUsername();

    String getEmail();

    int getAge();

    LocalDateTime getCreatedAt();

    Set<Role> getRoles();
}

