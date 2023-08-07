package uk.gov.hmcts.reform.civil.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.civil.exceptions.ApplicationException;
import uk.gov.hmcts.reform.civil.exceptions.ErrorDetails;
import uk.gov.hmcts.reform.civil.model.casedata.YesOrNo;
import uk.gov.hmcts.reform.civil.requestbody.AddressType;
import uk.gov.hmcts.reform.civil.requestbody.ClaimantType;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.requestbody.DefendantType;
import uk.gov.hmcts.reform.civil.requestbody.Interest;
import uk.gov.hmcts.reform.civil.validation.PostcodeValidator;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CreateClaimFromSdtServiceTest {

    @Autowired
    private CreateClaimFromSdtService createClaimFromSdtService;

    @MockBean
    private PostcodeValidator postcodeValidator;

    private static final String AUTHORIZATION = "Bearer user1";
    private static final String sdtRequestId = "sdtRequestId";

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
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT,sdtRequestId))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldThrowPaymentExceptionWhenPaymentNotMadeInCCD() {

        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("12345678")
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
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT,sdtRequestId))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldThrowClaimantValidationExceptionWhenClaimantInCCD() {

        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("12345678")
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
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT,sdtRequestId))
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
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("12345678")
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

    @Test
    void shouldThrowPostcodeExceptionWhenPostcodeInvalid1v1() {
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("12345678")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("isValid")
            .claimant(ClaimantType.builder().name("validClaimant").address(AddressType.builder().postcode("BR1zz1LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").address(AddressType.builder().postcode("BR1zz1LS").build())
                            .build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        when(postcodeValidator.validate(any())).thenReturn(List.of("Postcode must be in England or Wales"));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> createClaimFromSdtService
            .validateRequestParams(createClaimSDT));

        assertEquals(ErrorDetails.INVALID_DEFENDANT1_POSTCODE, exception.getErrorDetails());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

    }

    @Test
    void shouldThrowPostcodeExceptionWhenPostcodeInvalid1v2() {
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("12345678")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("isValid")
            .claimant(ClaimantType.builder().name("validClaimant").address(AddressType.builder().postcode("BR1zz1LS").build())
                          .build())
            .defendant2(DefendantType.builder().name("defendant2").address(AddressType.builder().postcode("BR1zz1LS").build())
                            .build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        when(postcodeValidator.validate(any())).thenReturn(List.of("Postcode must be in England or Wales"));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> createClaimFromSdtService
            .validateRequestParams(createClaimSDT));

        assertEquals(ErrorDetails.INVALID_DEFENDANT2_POSTCODE, exception.getErrorDetails());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

}
