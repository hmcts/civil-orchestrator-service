package uk.gov.hmcts.reform.civil.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        ConstraintViolationException.class,
    })
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode("1");
        response.setErrorText(ex.getMessage());
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }
}
