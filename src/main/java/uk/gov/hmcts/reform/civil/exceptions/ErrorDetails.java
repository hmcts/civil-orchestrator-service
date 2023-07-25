package uk.gov.hmcts.reform.civil.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorDetails {

    INVALID_DATA("000", "Bad data"),
    UNKNOWN_USER("001", "Unknown User"),
    INVALID_PAYMENT("003", "D/D facility not set"),
    INVALID_CLAIMANT_DETAILS("005", " claimant details missing"),

    INVALID_EXTERNAL_DATA("1002", "Invalid external data"),
    INVALID_DATA_CUSTOM("1003", "Invalid data with following error: ");

    private final String errorCode;
    private final String errorText;
}
