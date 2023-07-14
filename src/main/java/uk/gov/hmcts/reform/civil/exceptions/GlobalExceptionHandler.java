package uk.gov.hmcts.reform.civil.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        ApplicationException.class,
    })
    public ResponseEntity<?> handleApplicationException(ApplicationException ex) {
        log.error("ApplicationException thrown. Cause: {}",
                ex.getErrorDetails().getErrorText(), ex);
        ApiError apiError =
                new ApiError(
                        ex.getErrorDetails().getErrorCode().toString(),
                        ex.getErrorDetails().getErrorText()
                                + (Objects.nonNull(ex.getCustomMessage())
                                    ?ex.getCustomMessage():""));
        return new ResponseEntity<>(apiError, ex.getStatus());
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
    })
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex) {
        List<String> messages = new ArrayList<String>();
        for (Object error : ex.getDetailMessageArguments()) {
            messages.add(error.toString());
        }
        log.error("Handling MethodArgumentNotValidException|" + messages);
        ApiError apiError =
            new ApiError(
                ErrorDetails.INVALID_DATA.getErrorCode().toString(),
                ErrorDetails.INVALID_DATA.getErrorText());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
