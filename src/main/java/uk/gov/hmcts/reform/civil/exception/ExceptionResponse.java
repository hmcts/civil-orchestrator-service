package uk.gov.hmcts.reform.civil.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
    private String errorCode;
    private String errorText;
}
