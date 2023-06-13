package uk.gov.hmcts.reform.civil.mappings;

import uk.gov.hmcts.reform.civil.modelsdt.CreateClaimSDT;

public class CreateClaimMapper {

    public CreateClaimCCD mappedCreateClaim(CreateClaimSDT createClaimSDT) {
        CreateClaimCCD createClaimCCD = CreateClaimMapperInterface.INSTANCE.claimToDto(createClaimSDT);

        createClaimCCD.setInterestFromSpecificDateDescription("test description interest from");

        System.out.println(createClaimCCD);
        return  createClaimCCD;
    }



}
