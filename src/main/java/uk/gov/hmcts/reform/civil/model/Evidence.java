package uk.gov.hmcts.reform.civil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;


@Data
@Builder

public class Evidence {

    private final EvidenceDetails value;
    @JsonIgnore
    private final String id;
}
