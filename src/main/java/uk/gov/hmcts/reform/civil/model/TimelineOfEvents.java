package uk.gov.hmcts.reform.civil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class TimelineOfEvents {

    public final TimelineOfEventDetails value;
    @JsonIgnore
    public final String id;
}
