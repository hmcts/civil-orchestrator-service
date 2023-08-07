package uk.gov.hmcts.reform.civil.exceptions;

import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({
        ApplicationException.class,
    })
    public ResponseEntity<?> handleApplicationException(ApplicationException ex) {
        LOG.error("ApplicationException thrown. Cause: {}",
                ex.getErrorDetails().getErrorText(), ex);
        ApiError apiError =
                new ApiError(
                        ex.getErrorDetails().getErrorCode().toString(),
                        ex.getErrorDetails().getErrorText() + ", "
                                + (Objects.nonNull(ex.getCustomMessage())
                                ? ex.getCustomMessage() : ""));
        return new ResponseEntity<>(apiError, ex.getStatus());
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
    })
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex) {
        List<String> messages = new ArrayList<String>();
        for (Object error : ex.getDetailMessageArguments()) {
            messages.add(error.toString());
            if (error.toString().contains("Defendant 1")){
                LOG.error("Handling MethodArgumentNotValidException|" + messages);
                ApiError apiError =
                    new ApiError(
                        ErrorDetails.INVALID_DEFENDANT1_POSTCODE.getErrorCode(),
                        ErrorDetails.INVALID_DEFENDANT1_POSTCODE.getErrorText());
                return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
            }
            if (error.toString().contains("Defendant 2")){
                LOG.error("Handling MethodArgumentNotValidException|" + messages);
                ApiError apiError =
                    new ApiError(
                        ErrorDetails.INVALID_DEFENDANT2_POSTCODE.getErrorCode(),
                        ErrorDetails.INVALID_DEFENDANT2_POSTCODE.getErrorText());
                return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
            }
        }
        LOG.error("Handling MethodArgumentNotValidException|" + messages);
        ApiError apiError =
                new ApiError(
                        ErrorDetails.INVALID_DATA.getErrorCode(),
                        ErrorDetails.INVALID_DATA.getErrorText());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        ValidationException.class,
    })
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        String messages = ex.getMessage();
        LOG.error("Handling ValidationException | " + messages);
        LOG.error("local message | " + ex.getLocalizedMessage());
        ApiError apiError =
            new ApiError(
                ErrorDetails.INVALID_DATA.getErrorCode().toString(),
                ex.getCause().getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
