package uk.gov.hmcts.reform.civil.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<CreateClaimErrorResponse> handleConstraintViolationException(final HttpServletRequest request,
                                                                                       final Exception exception) {
        LOG.error(exception.getMessage());

        final CreateClaimErrorResponse error = new CreateClaimErrorResponse().toBuilder()
            .errorCode("002")
            .errorText(exception.getCause().getMessage()).build();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<CreateClaimErrorResponse> handleValidationException(final HttpServletRequest request,
                                                                                       final Exception exception) {
        LOG.error(exception.getMessage());

        final CreateClaimErrorResponse error = new CreateClaimErrorResponse().toBuilder()
            .errorCode("001")
            .errorText(exception.getCause().getMessage()).build();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(InvalidUserException.class)
    @ResponseBody
    public ResponseEntity<CreateClaimErrorResponse> handleInvalidUserException(HttpServletRequest request, Exception exception) {
        LOG.warn(exception.getMessage(), exception);

        final CreateClaimErrorResponse error = new CreateClaimErrorResponse().toBuilder()
            .errorCode("401")
            .errorText(exception.getCause().getMessage()).build();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<CreateClaimErrorResponse> handleBadRequestException(HttpServletRequest request, Exception exception) {
        LOG.warn(exception.getMessage(), exception);

        final CreateClaimErrorResponse error = new CreateClaimErrorResponse().toBuilder()
            .errorCode("201")
            .errorText(exception.getCause().getMessage()).build();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(ClaimantValidationException.class)
    @ResponseBody
    public ResponseEntity<CreateClaimErrorResponse> handleClaimantValidationRequestException(HttpServletRequest request, Exception exception) {
        LOG.warn(exception.getMessage(), exception);

        final CreateClaimErrorResponse error = new CreateClaimErrorResponse().toBuilder()
            .errorCode("005")
            .errorText(exception.getCause().getMessage()).build();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseBody
    public ResponseEntity<CreateClaimErrorResponse> handlePaymentException(HttpServletRequest request, Exception exception) {
        LOG.warn(exception.getMessage(), exception);

        final CreateClaimErrorResponse error = new CreateClaimErrorResponse().toBuilder()
            .errorCode("003")
            .errorText(exception.getCause().getMessage()).build();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

}
