package uk.gov.hmcts.reform.civil.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorDetails errorDetails;
    private final HttpStatus status;
    private final String customMessage;

    public ApplicationException(ErrorDetails errorDetails,
                                HttpStatus httpStatus, String customMessage) {
        super(errorDetails.toString());
        this.errorDetails = errorDetails;
        this.status = httpStatus;
        this.customMessage = customMessage;
    }

    public ApplicationException(ErrorDetails errorDetails, HttpStatus httpStatus) {
        super(errorDetails.toString());
        this.errorDetails = errorDetails;
        this.status = httpStatus;
        this.customMessage = null;
    }
}
