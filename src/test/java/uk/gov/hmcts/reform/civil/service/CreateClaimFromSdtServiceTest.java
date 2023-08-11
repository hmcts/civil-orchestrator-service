package uk.gov.hmcts.reform.civil.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.civil.exceptions.ApplicationException;
import uk.gov.hmcts.reform.civil.model.casedata.YesOrNo;
import uk.gov.hmcts.reform.civil.model.prd.CivilServiceApi;
import uk.gov.hmcts.reform.civil.requestbody.AddressType;
import uk.gov.hmcts.reform.civil.requestbody.ClaimantType;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.requestbody.DefendantType;
import uk.gov.hmcts.reform.civil.requestbody.Interest;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CreateClaimFromSdtServiceTest {

    @Autowired
    private CreateClaimFromSdtService createClaimFromSdtService;

    @MockBean
    private UserService userService;

    @MockBean
    CivilServiceApi civilServiceApi;

    private static final String AUTHORIZATION = "Bearer user1";
    private static final String sdtRequestId = "sdtRequestId";

    @Test
    void shouldSendInvalidUserWhenBulkIdFromSDTPresentInCCD() {

        Mockito.when(civilServiceApi.validatePostCode(anyString(),anyString())).thenReturn(new ArrayList<>());
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").address(AddressType.builder().postcode("BR11LS").build()).build())
            .defendant2(DefendantType.builder().name("defendant2").address(AddressType.builder().postcode("BR11LS").build()).build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT,sdtRequestId))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldThrowPaymentExceptionWhenPaymentNotMadeInCCD() {

        Mockito.when(civilServiceApi.validatePostCode(anyString(),anyString())).thenReturn(new ArrayList<>());
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("testIdamIDMatchesBulkId")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("valid")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").address(AddressType.builder().postcode("BR11LS").build()).build())
            .defendant2(DefendantType.builder().name("defendant2").address(AddressType.builder().postcode("BR11LS").build()).build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT,sdtRequestId))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldThrowExceptionWhenFirstDefendantAddressIsNotValid() {

        Mockito.when(civilServiceApi.validatePostCode(anyString(),anyString())).thenReturn(new ArrayList<>(Collections.singleton(
            "Postcode must be in England or Wales")));
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("testIdamIDMatchesBulkId")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("valid")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").address(AddressType.builder().postcode("7686868RF").build()).build())
            .defendant2(DefendantType.builder().name("defendant2").address(AddressType.builder().postcode("BR11LS").build()).build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT,sdtRequestId))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldThrowExceptionWhenSecondDefendantAddressIsNotValid() {

        Mockito.when(civilServiceApi.validatePostCode(anyString(),anyString())).thenReturn(new ArrayList<>(Collections.singleton(
            "Postcode must be in England or Wales")));
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("testIdamIDMatchesBulkId")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("valid")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").address(AddressType.builder().postcode("BR11LS").build()).build())
            .defendant2(DefendantType.builder().name("defendant2").address(AddressType.builder().postcode("7686868RF").build()).build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT,sdtRequestId))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldThrowClaimantValidationExceptionWhenClaimantInCCD() {

        Mockito.when(civilServiceApi.validatePostCode(anyString(),anyString())).thenReturn(new ArrayList<>());
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("testIdamIDMatchesBulkId")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").address(AddressType.builder().postcode("BR11LS").build()).build())
            .defendant2(DefendantType.builder().name("defendant2").address(AddressType.builder().postcode("7686868RF").build()).build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        assertThatThrownBy(() -> createClaimFromSdtService.buildResponse(AUTHORIZATION,createClaimSDT,sdtRequestId))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void shouldCheckSdtRequest() {
        Mockito.when((userService.getUserInfo(anyString()))).thenReturn(UserInfo.builder().build());
        Mockito.when(civilServiceApi.searchCaseForCaseworker(anyString(), anyString(), anyString())).thenReturn(false);
        String sdtRequestId = "unique";
        Boolean requestIdFromCCD = createClaimFromSdtService.validateSdtRequest(AUTHORIZATION,sdtRequestId);
        assertEquals(requestIdFromCCD,false);
    }

    @Test
    void shouldReturnCCDClaim() {
        Mockito.when(civilServiceApi.validatePostCode(anyString(),anyString())).thenReturn(new ArrayList<>());
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
