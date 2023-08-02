package uk.gov.hmcts.reform.civil.mappings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimMapper {

    private CreateClaimCCD createClaimCCD;

    public CreateClaimCCD mappedCreateClaim(CreateClaimRequest createClaimRequest) {
        createClaimCCD = CreateClaimMapperInterface.INSTANCE.claimToDto(createClaimRequest);

        return  createClaimCCD;
    }

}
