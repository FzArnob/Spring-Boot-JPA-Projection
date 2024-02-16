package fzs.lab.jpa.projection.response;

public enum SuccessMessages {
    USER_CREATED("Successfully created user: %s"),
    USER_UPDATED("Successfully updated user: %s"),
    USER_DETAIL("Successfully fetched user with id: %s"),
    USER_LIST("Successfully fetched total: %s users"),
    USER_DELETED("Successfully deleted user with id: %s"),
    ROLE_CREATED("Successfully created role: %s"),
    ROLE_UPDATED("Successfully updated role: %s"),
    ROLE_DETAIL("Successfully fetched role with id: %s"),
    ROLE_LIST("Successfully fetched total: %s roles"),
    ROLE_DELETED("Successfully deleted role with id: %s");
    private final String message;

    SuccessMessages(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
