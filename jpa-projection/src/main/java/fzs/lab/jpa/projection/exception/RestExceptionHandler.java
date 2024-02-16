package fzs.lab.jpa.projection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.*;
import java.util.HashMap;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @AllArgsConstructor
    @Getter
    public static class ResourceNotFoundException extends Exception {
        private final String message;
    }
    @AllArgsConstructor
    @Getter
    public static class ResourceAlreadyExistsException extends Exception {
        private final String message;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(ResourceAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({
            SQLException.class,
            SQLDataException.class,
            SQLIntegrityConstraintViolationException.class,
            SQLSyntaxErrorException.class,
            SQLTimeoutException.class,
            SQLTransactionRollbackException.class,
            SQLFeatureNotSupportedException.class,
            BatchUpdateException.class,
            SQLNonTransientException.class,
            SQLTransientException.class,
            SQLRecoverableException.class,
            SQLClientInfoException.class
    })
    public ResponseEntity<ErrorResponse> handleSqlExceptions(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorMessages.DATABASE_ERROR.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleConstraintViolation(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(ErrorMessages.BAD_REQUEST.getMessage(), new HashMap<>() {{
                    ex.getBindingResult().getFieldErrors().forEach(fieldError -> put(fieldError.getField(), fieldError.getDefaultMessage()));
                }}));
    }

    @ExceptionHandler({
            BindException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<Object> handleBindException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorMessages.BIND_ERROR.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error(ex.getClass().getName() + ": " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorMessages.INTERNAL_ERROR.getMessage()));
    }
}
