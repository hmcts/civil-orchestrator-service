package uk.gov.hmcts.reform.civil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.gov.hmcts.reform.civil.enums.YesOrNo;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimMapperInterface;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimSDT;
import uk.gov.hmcts.reform.civil.model.StatementOfTruth;
import uk.gov.hmcts.reform.civil.modelsdt.AddressType;
import uk.gov.hmcts.reform.civil.modelsdt.ClaimantType;
import uk.gov.hmcts.reform.civil.modelsdt.DefendantType;

import java.time.LocalDate;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);

        CreateClaimSDT createClaimSDT = new CreateClaimSDT();
        createClaimSDT.setClaimantReference("claimant sol reference");
        createClaimSDT.setClaimant(ClaimantType.builder()
                                       .name("a organisation name")
                                       .address(AddressType.builder()
                                                    .line1("claimant line one")
                                                    .line2("claimant line two")
                                                    .line3("claimant line three")
                                                    .line4("claimant line four")
                                                    .postcode("claimant postcode")
                                                    .build())
            .build());

        createClaimSDT.setDefendant1(DefendantType.builder()
                                       .name("Mr defendant one")
                                       .address(AddressType.builder()
                                                    .line1("defendant line one")
                                                    .line2("defendant line two")
                                                    .line3("defendant line three")
                                                    .line4("defendant line four")
                                                    .postcode("defendant postcode")
                                                    .build())
                                       .build());

        createClaimSDT.setDefendant2(DefendantType.builder()
                                         .name("Mrs defendant two")
                                         .address(AddressType.builder()
                                                      .line1("defendant two line one")
                                                      .line2("defendant two line two")
                                                      .line3("defendant two line three")
                                                      .line4("defendant two line four")
                                                      .postcode("defendant two postcode")
                                                      .build())
                                         .build());

        createClaimSDT.setParticulars("particulars of claim");
        createClaimSDT.setSotSignature("12345");
        createClaimSDT.setClaimAmount(12345);
        createClaimSDT.setReserveRightToClaimInterest(Boolean.TRUE);
        createClaimSDT.setClaimDate(LocalDate.now().minusDays(10));
        createClaimSDT.setSotSignature("a test signature");

        CreateClaimCCD createClaimCCD = CreateClaimMapperInterface.INSTANCE.claimToDto(createClaimSDT);

        //defaulted values
        createClaimCCD.setUiStatementOfTruth(StatementOfTruth.builder().role("bulk claim issuer")
                                                 .name(createClaimSDT.getSotSignature()).build());

        System.out.println(createClaimCCD);
    }

}
