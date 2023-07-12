package uk.gov.hmcts.reform.civil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(final String message) {
        super(message);
    }
}
