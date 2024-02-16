package fzs.lab.jpa.projection.dto;

public interface RoleDto {
    // Add other fields as needed from the User entity
    // No need for setters as it's a jpa projection
    Long getId();
    String getName();
    int getTotalPermissions();
}

