package uk.gov.hmcts.reform.civil.mappings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.civil.modelsdt.CreateClaimSDT;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimMapper {

    private CreateClaimCCD createClaimCCD;

    public CreateClaimCCD mappedCreateClaim(CreateClaimSDT createClaimSDT) {
        createClaimCCD = CreateClaimMapperInterface.INSTANCE.claimToDto(createClaimSDT);

        createClaimCCD.setInterestFromSpecificDateDescription("test description interest from");

        System.out.println(createClaimCCD);
        return  createClaimCCD;
    }



}
