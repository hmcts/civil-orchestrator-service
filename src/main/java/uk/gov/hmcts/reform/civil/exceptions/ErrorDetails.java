package uk.gov.hmcts.reform.civil.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorDetails {
    INVALID_DATA(1001, "Invalid data");

    private final Integer errorCode;
    private final String errorText;
}
