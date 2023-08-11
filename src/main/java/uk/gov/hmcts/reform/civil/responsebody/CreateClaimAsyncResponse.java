package uk.gov.hmcts.reform.civil.responsebody;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CreateClaimAsyncResponse{

    private LocalDate issueDate;
    private BigDecimal fee;
}
