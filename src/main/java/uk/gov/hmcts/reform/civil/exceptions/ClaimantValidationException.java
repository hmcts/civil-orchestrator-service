package uk.gov.hmcts.reform.civil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ClaimantValidationException extends RuntimeException {

    public ClaimantValidationException(final String message) {
        super(message);
    }
}
