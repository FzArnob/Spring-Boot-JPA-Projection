package fzs.lab.jpa.projection.response;

public enum SuccessMessages {
    USER_CREATED("Successfully created user: %s"),
    USER_DETAIL("Successfully fetched user with id: %s"),
    USER_LIST("Successfully fetched total: %s users"),
    USER_DELETED("Successfully deleted user with id: %s");

    private final String message;

    SuccessMessages(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
