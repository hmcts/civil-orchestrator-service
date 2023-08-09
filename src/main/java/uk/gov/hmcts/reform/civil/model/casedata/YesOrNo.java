package uk.gov.hmcts.reform.civil.model.casedata;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum YesOrNo {
    @JsonProperty("Yes")
    YES,
    @JsonProperty("No")
    NO
}
