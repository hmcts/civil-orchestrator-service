package uk.gov.hmcts.reform.civil.responsebody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimCCD;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClaimResponseBody {
    private CreateClaimCCD data;
    private String event;
}
