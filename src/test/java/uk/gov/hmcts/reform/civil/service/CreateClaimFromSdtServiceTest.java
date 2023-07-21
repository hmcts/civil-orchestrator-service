package uk.gov.hmcts.reform.civil.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.civil.exceptions.ApplicationException;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.requestbody.AddressType;
import uk.gov.hmcts.reform.civil.requestbody.ClaimantType;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.requestbody.DefendantType;
import uk.gov.hmcts.reform.civil.requestbody.Interest;
import uk.gov.hmcts.reform.civilcommonsmock.civil.enums.YesOrNo;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CreateClaimFromSdtServiceTest {

    @Autowired
    private CreateClaimFromSdtService createClaimFromSdtService;

    private static final String AUTHORIZATION = "Bearer user1";

    @Test
    void shouldSendInvalidUserWhenBulkIdFromSDTPresentInCCD() {

        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldThrowPaymentExceptionWhenPaymentNotMadeInCCD() {

        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("testIdamIDMatchesBulkId")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("valid")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldThrowClaimantValidationExceptionWhenClaimantInCCD() {

        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("testIdamIDMatchesBulkId")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldCheckSdtRequest() {
        String sdtRequestId = "unique";
        String requestIdFromCCD = createClaimFromSdtService.getSdtRequestId();
        assertEquals(sdtRequestId,requestIdFromCCD);
    }

    @Test
    void shouldReturnCCDClaim() {
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("testIdamIDMatchesBulkId")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant1").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .sotSignature("sotSignatureExample")
            .reserveRightToClaimInterest(true)
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        CreateClaimCCD claimCCD = createClaimFromSdtService.processSdtClaim(createClaimSDT);
        assertEquals(claimCCD.getClaimInterest(), YesOrNo.YES);
    }
}