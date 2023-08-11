package uk.gov.hmcts.reform.civil.requestbody;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CreateClaimAsyncRequest {
    private LocalDate issueDate;
    private BigDecimal fee;
}
