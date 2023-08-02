package uk.gov.hmcts.reform.civil.mappings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.reform.civil.model.casedata.Address;
import uk.gov.hmcts.reform.civil.model.casedata.CorrectEmail;
import uk.gov.hmcts.reform.civil.model.casedata.Party;
import uk.gov.hmcts.reform.civil.model.casedata.SolicitorReferences;
import uk.gov.hmcts.reform.civil.model.casedata.StatementOfTruth;
import uk.gov.hmcts.reform.civil.model.casedata.YesOrNo;
import uk.gov.hmcts.reform.civil.requestbody.AddressType;
import uk.gov.hmcts.reform.civil.requestbody.ClaimantType;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.requestbody.DefendantType;
import uk.gov.hmcts.reform.civil.requestbody.Interest;
import uk.gov.hmcts.reform.civil.utils.MonetaryConversions;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateClaimMapperInterfaceTest {

    private final CreateClaimMapperInterface mapper = Mappers.getMapper(CreateClaimMapperInterface.class);
    private CreateClaimRequest createClaimRequest;
    private CreateClaimCCD createClaimCCD;
    private CreateClaimCCD testCreateClaimCCD;

    @BeforeEach
    void setup() {
        // example SDT request claim data
        createClaimRequest = CreateClaimRequest.builder()
            .sdtRequestId("StdRequestIdExample")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("bulk organisation")
                          .address(AddressType.builder().addressLine1("line 1")
                                       .addressLine2("line 2").postcode("postcode").build()).build())
            .reserveRightToClaimInterest(false)
            .defendant1(DefendantType.builder().name("Mr Defendant1")
                            .address(AddressType.builder().addressLine1("Mr Defendant1 line 1")
                                         .addressLine2("Mr Defendant1 line 2").postcode("Mr Defendant1 postcode").build()).build())
            .claimAmount(1111L)
            .reserveRightToClaimInterest(false)
            .sotSignature("sotSignatureName")
            .particulars("particulars of claim")
            .build();
        // Mapping SDT to CCD fields
        createClaimCCD = mapper.claimToDto(createClaimRequest);
        // CCD test mapping result
        testCreateClaimCCD = CreateClaimCCD.builder()
            .sdtRequestIdFromSdt("StdRequestIdExample")
            .applicant1(Party.builder().build())
            .applicant1(Party.builder()
                            .type(Party.Type.ORGANISATION)
                            .organisationName("bulk organisation")
                            .primaryAddress(Address.builder()
                                                .addressLine1("line 1")
                                                .addressLine2("line 2")
                                                .postCode("postcode")

                                                .build())
                            .build())
            .applicantSolicitor1CheckEmail(CorrectEmail.builder()
                                               .correct(YesOrNo.NO)
                                               .build())
            .solicitorReferences(SolicitorReferences.builder()
                                     .applicantSolicitor1Reference("1568h8992334")
                                     .build())
            .respondent1(Party.builder()
                             .type(Party.Type.INDIVIDUAL)
                             .bulkClaimPartyName("Mr Defendant1")
                             .primaryAddress(Address.builder()
                                                 .addressLine1("Mr Defendant1 line 1")
                                                 .addressLine2("Mr Defendant1 line 2")
                                                 .postCode("Mr Defendant1 postcode")
                                                 .build())
                             .build())
            .adddRespondent2(YesOrNo.NO)
            .interestFromSpecificDate(null)
            .interestFromSpecificDateDescription(null)
            .totalClaimAmount(MonetaryConversions.penniesToPounds(BigDecimal.valueOf(1111L)))
            .claimInterest(YesOrNo.NO)
            .detailsOfClaim("particulars of claim")
            .uiStatementOfTruth(StatementOfTruth.builder()
                                    .name("sotSignatureName")
                                    .role("bulk issuer role")
                                    .build())
            .build();

    }

    @Test
    void shouldNotMapFields_whenInvokedAndMappingNull() {
        // Mapping
        createClaimCCD = mapper.claimToDto(null);
        // Then
        assertThat(createClaimCCD).isNull();
    }

    @Test
    void shouldMapFieldsRequiredFor1v1NoInterest_whenInvoked() {
        // Mapping
        createClaimCCD = mapper.claimToDto(createClaimRequest);
        // Then
        assertThat(createClaimCCD).usingRecursiveComparison().isEqualTo(testCreateClaimCCD);
    }

    // TODO: Updated tests when interest claim options are finalised
    @Test
    void shouldMapFieldsRequiredFor1v1WithInterestAdded_whenInvoked() {
        // fields for 1v1 interest
        createClaimRequest.setReserveRightToClaimInterest(true);
        createClaimRequest.setInterest(Interest.builder()
                                           .interestClaimDate(LocalDate.now().minusMonths(2))
                                           .interestOwedDate(LocalDate.now().minusMonths(1))
                                           .build());
        // Mapping
        createClaimCCD = mapper.claimToDto(createClaimRequest);
        // Updated CCD test mappings should then be
        testCreateClaimCCD.setClaimInterest(YesOrNo.YES);
        testCreateClaimCCD.setInterestFromSpecificDate(LocalDate.now().minusMonths(1));
        //
        assertThat(createClaimCCD).usingRecursiveComparison().isEqualTo(testCreateClaimCCD);
    }

    @Test
    void shouldMapFieldsRequiredFor1v2NoInterest_whenInvoked() {
        // fields for 1v2
        createClaimRequest.setDefendant2(DefendantType.builder()
                                             .name("Mr Defendant2")
                                             .address(AddressType.builder()
                                                          .addressLine1("Mr Defendant2 line 1")
                                                          .addressLine2("Mr Defendant2 line 2")
                                                          .postcode("Mr Defendant2 postcode")
                                                          .build())
                                             .build());
        // Mapping
        createClaimCCD = mapper.claimToDto(createClaimRequest);
        // Updated CCD test mappings should then be
        testCreateClaimCCD.setAdddRespondent2(YesOrNo.YES);
        testCreateClaimCCD.setRespondent2(Party.builder()
                                              .type(Party.Type.INDIVIDUAL)
                                              .bulkClaimPartyName("Mr Defendant2")
                                              .primaryAddress(Address.builder()
                                                                  .addressLine1("Mr Defendant2 line 1")
                                                                  .addressLine2("Mr Defendant2 line 2")
                                                                  .postCode("Mr Defendant2 postcode")
                                                                  .build())
                                              .build());
        // Then
        assertThat(createClaimCCD).usingRecursiveComparison().isEqualTo(testCreateClaimCCD);
    }

    // TODO: Updated tests when interest claim options are finalised
    @Test
    void shouldMapFieldsRequiredFor1v2WithInterestAdded_whenInvoked() {
        // fields for 1v2 interest
        createClaimRequest.setReserveRightToClaimInterest(true);
        createClaimRequest.setDefendant2(DefendantType.builder()
                                             .name("Mr Defendant2")
                                             .address(AddressType.builder()
                                                          .addressLine1("Mr Defendant2 line 1")
                                                          .addressLine2("Mr Defendant2 line 2")
                                                          .postcode("Mr Defendant2 postcode")
                                                          .build())
                                             .build());
        createClaimRequest.setInterest(Interest.builder()
                                           .interestClaimDate(LocalDate.now().minusMonths(2))
                                           .interestOwedDate(LocalDate.now().minusMonths(1))
                                           .build());
        // Mapping
        createClaimCCD = mapper.claimToDto(createClaimRequest);
        // Updated CCD test mappings should then be
        testCreateClaimCCD.setClaimInterest(YesOrNo.YES);
        testCreateClaimCCD.setInterestFromSpecificDate(LocalDate.now().minusMonths(1));
        testCreateClaimCCD.setAdddRespondent2(YesOrNo.YES);
        testCreateClaimCCD.setRespondent2(Party.builder()
                                              .type(Party.Type.INDIVIDUAL)
                                              .bulkClaimPartyName("Mr Defendant2")
                                              .primaryAddress(Address.builder()
                                                                  .addressLine1("Mr Defendant2 line 1")
                                                                  .addressLine2("Mr Defendant2 line 2")
                                                                  .postCode("Mr Defendant2 postcode")
                                                                  .build())
                                              .build());
        // Then
        assertThat(createClaimCCD).usingRecursiveComparison().isEqualTo(testCreateClaimCCD);
    }

}


