package uk.gov.hmcts.reform.civil.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorDetails errorDetails;
    private final HttpStatus status;

    public ApplicationException(ErrorDetails errorDetails, HttpStatus httpStatus) {
        super(errorDetails.toString());
        this.errorDetails = errorDetails;
        this.status = httpStatus;
    }
}
