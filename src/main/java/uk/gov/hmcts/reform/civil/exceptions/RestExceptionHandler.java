package uk.gov.hmcts.reform.civil.exceptions;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.reform.civil.model.SdtErrorResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);




    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<SdtErrorResponse> handleConstraintViolationException(final HttpServletRequest request,
                                                                     final Exception exception) {
        LOG.error(exception.getMessage());

        final SdtErrorResponse sdtErrorResponse = new SdtErrorResponse().builder()
            .errorText(exception.getCause().getMessage()).errorCode("002").build();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(sdtErrorResponse);
    }

/*    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .collect(Collectors.toList());
        final SdtErrorResponse sdtErrorResponse = new SdtErrorResponse().builder()
            .errorText(ex.getLocalizedMessage()).errorCode("002").build();
        return handleExceptionInternal(ex, sdtErrorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }*/

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleException(final HttpServletRequest request, final Exception exception) {
        LOG.error(exception.getMessage(), exception);

        Throwable targetException;

        if (exception instanceof HttpStatusCodeException || exception instanceof FeignException) {
            targetException = exception;
        } else {
            targetException = exception.getCause();
        }

        HttpStatus httpStatus = (targetException != null) ? getHttpStatus(targetException) : null;

        final SdtErrorResponse sdtErrorResponse = new SdtErrorResponse().builder()
            .errorText(exception.getCause().getMessage()).errorCode("001").build();

        return ResponseEntity
            .status(httpStatus)
            .body(sdtErrorResponse);
    }

    private HttpStatus getHttpStatus(final Throwable causeOfException) {
        HttpStatus httpStatus = checkAndRetrieveExceptionStatusCode(causeOfException);
        if (httpStatus != null) {
            return assignExceptionHttpCode(httpStatus);
        }

        if (isReadTimeoutException(causeOfException)) {
            return HttpStatus.GATEWAY_TIMEOUT;
        }

        if (isUnknownHostException(causeOfException)) {
            return HttpStatus.BAD_GATEWAY;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private HttpStatus checkAndRetrieveExceptionStatusCode(final Throwable causeOfException) {
        HttpStatus httpStatus = null;
        if (causeOfException instanceof HttpStatusCodeException) {
            httpStatus = HttpStatus.valueOf(((HttpStatusCodeException) causeOfException).getRawStatusCode());
        } else if (causeOfException instanceof FeignException.FeignClientException
            || causeOfException instanceof FeignException.FeignServerException) {
            httpStatus = HttpStatus.valueOf(((FeignException) causeOfException).status());
        }

        return httpStatus;
    }

    private HttpStatus assignExceptionHttpCode(final HttpStatus httpStatus) {
        if (httpStatus.is5xxServerError()) {
            if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
                return HttpStatus.BAD_GATEWAY;
            }

            return httpStatus;
        }

        if (httpStatus == HttpStatus.UNAUTHORIZED) {
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private boolean isReadTimeoutException(final Throwable causeOfException) {
        Throwable innerException = causeOfException.getCause();
        return innerException instanceof java.net.SocketTimeoutException
            && innerException.getMessage().contains("Read timed out");
    }

    private boolean isUnknownHostException(final Throwable causeOfException) {
        return causeOfException instanceof java.net.UnknownHostException;
    }


}
