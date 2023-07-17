package uk.gov.hmcts.reform.civil.exceptions;

import lombok.Getter;

@Getter
public class ApiError {
    private final String errorCode;
    private final String errorText;

    public ApiError(String errorCode, String errorText) {
        this.errorCode = errorCode;
        this.errorText = errorText;
    }

    @Override
    public String toString() {
        return "ApiError [errorCode=" + errorCode + ", errorText=" + errorText + "]";
    }
}
