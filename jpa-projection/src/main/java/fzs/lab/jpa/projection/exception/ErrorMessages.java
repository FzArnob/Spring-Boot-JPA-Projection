package fzs.lab.jpa.projection.exception;

public enum ErrorMessages {
    USER_NOT_FOUND("User not found with id: %s"),
    ROLE_NOT_FOUND("Role not found with id: %s"),
    EMAIL_ALREADY_EXISTS("Email already exists: %s"),
    ROLE_ALREADY_EXISTS("Email already exists: %s"),
    EMAIL_NOT_REGISTERED("Email is not registered: %s"),
    INTERNAL_ERROR("Internal server error!");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
