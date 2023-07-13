package uk.gov.hmcts.reform.civil.exceptions;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ApplicationException.class,
    })
    public ResponseEntity<?> handleApplicationException(ApplicationException ex) {
        log.error("ApplicationException thrown. Cause: {}", ex.getErrorDetails().getErrorText(), ex);
        ApiError apiError =
                new ApiError(
                        ex.getErrorDetails().getErrorCode().toString(),
                        ex.getErrorDetails().getErrorText());
        return new ResponseEntity<>(apiError, ex.getStatus());
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
    })
    public ResponseEntity<?> handleException(ConstraintViolationException ex) {
        List<String> messages = new ArrayList<String>();
        for (ConstraintViolation error : ex.getConstraintViolations()) {
            messages.add(error.getMessage());
        }
        log.error("Handling ConstraintViolationException|" + messages);
        ApiError apiError =
                new ApiError(
                        ErrorDetails.INVALID_DATA.getErrorCode().toString(),
                        ErrorDetails.INVALID_DATA.getErrorText());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
