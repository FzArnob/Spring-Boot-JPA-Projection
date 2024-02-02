package fzs.lab.jpa.projection.dto;

import java.time.LocalDateTime;

public interface UserDto {
    // Add other fields as needed from the User entity
    // No need for setters as it's a jpa projection
    Long getId();
    String getUsername();
    String getEmail();
}

