package uk.gov.hmcts.reform.civil.responsebody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimSyncResponse implements CreateClaimResponse {

    private String claimNumber;
    private LocalDate issueDate;
    private LocalDate serviceDate;
}
