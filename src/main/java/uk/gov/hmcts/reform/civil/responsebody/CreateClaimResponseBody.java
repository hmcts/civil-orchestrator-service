package uk.gov.hmcts.reform.civil.responsebody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimResponseBody {
    private CreateClaimCCD data;
    private String event;
}
