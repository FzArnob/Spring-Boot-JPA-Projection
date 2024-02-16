package fzs.lab.jpa.projection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final boolean success = false;
    private final String message;
}
