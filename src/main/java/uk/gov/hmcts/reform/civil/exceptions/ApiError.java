package uk.gov.hmcts.reform.civil.exceptions;

import lombok.Getter;

@Getter
public class ApiError {
    private final String errorId;
    private final String description;

    public ApiError(String errorId, String description) {
        this.errorId = errorId;
        this.description = description;
    }

    @Override
    public String toString() {
        return "ApiError [errorId=" + errorId + ", description=" + description + "]";
    }
}
