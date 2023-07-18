package uk.gov.hmcts.reform.civil.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorDetails {
    INVALID_DATA(000, "Bad data"),
    INVALID_EXTERNAL_DATA(1002, "Invalid external data"),
    INVALID_DATA_CUSTOM(1003, "Invalid data with following error: ");

    private final Integer errorCode;
    private final String errorText;
}