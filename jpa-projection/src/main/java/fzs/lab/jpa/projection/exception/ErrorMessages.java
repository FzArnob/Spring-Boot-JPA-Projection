package fzs.lab.jpa.projection.exception;

public enum ErrorMessages {
    USER_NOT_FOUND("User not found with id: %s"),
    EMAIL_ALREADY_EXISTS("Email already exists: %s"),
    INTERNAL_ERROR("Internal server error!");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
