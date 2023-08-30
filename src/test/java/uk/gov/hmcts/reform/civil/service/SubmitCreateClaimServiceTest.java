package uk.gov.hmcts.reform.civil.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.civil.model.prd.CivilServiceApi;
import uk.gov.hmcts.reform.civil.model.prd.model.Organisation;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponse;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmitCreateClaimServiceTest {

    @Mock
    OrganisationService organisationService;
    @Mock
    CivilServiceApi civilServiceApi;
    @Mock
    UserService userService;

    @Mock
    ObjectMapper objectMapper;

    SubmitCreateClaimService submitCreateClaimService;

    private String authorization = "Bearer 123";
    private String caseDetails;
    private CaseDetails civilResponseCaseDetails;


    @BeforeEach
    void setup() {

        submitCreateClaimService = new SubmitCreateClaimService(organisationService, civilServiceApi,
                                                                userService, objectMapper);

        given(userService.getUserInfo(anyString())).willReturn(UserInfo.builder().uid("uid").build());

        given(userService.getUserDetails(authorization)).willReturn(UserDetails.builder().id("userId").email("Test@test.com").build());

        given(organisationService.findOrganisation(anyString()))
            .willReturn(Optional.of(Organisation.builder().organisationIdentifier("Test Org id").build()));

        civilResponseCaseDetails = CaseDetails.builder().build();
        civilResponseCaseDetails.setData(new HashMap<>());
        String legacyCaseReference = "CIV12345";
        civilResponseCaseDetails.getData().put("legacyCaseReference", legacyCaseReference);

    }

    @Test
    void shouldCreateClaimSuccessFully_whenInvoked() throws JsonProcessingException {
        // Given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(caseDetails, HttpStatus.OK);
        Mockito.when(civilServiceApi.caseworkerSubmitEvent(anyString(), anyString(), any()))
            .thenReturn(responseEntity);

        given(objectMapper.readValue(caseDetails, CaseDetails.class)).willReturn(civilResponseCaseDetails);

        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    void shouldNotCreateClaimSuccessFully_whenInvokedAndUnauthorised() {
        // Given
        Mockito.when(civilServiceApi.caseworkerSubmitEvent(anyString(), anyString(), any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

    @Test
    void shouldNotCreateClaimSuccessFully_whenInvokedAndInvalidData() {
        // Given
        Mockito.when(civilServiceApi.caseworkerSubmitEvent(anyString(), anyString(), any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));
        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @Test
    void shouldNotCreateClaimSuccessFully_whenInvokedAndForbidden() {
        // Given
        Mockito.when(civilServiceApi.caseworkerSubmitEvent(anyString(), anyString(), any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));
        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void shouldNotCreateClaimSuccessFully_whenInvokedAndJsonException() throws JsonProcessingException {
        // Given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(caseDetails, HttpStatus.OK);
        given(objectMapper.readValue(caseDetails, CaseDetails.class)).willReturn(civilResponseCaseDetails);
        Mockito.when(civilServiceApi.caseworkerSubmitEvent(anyString(), anyString(), any()))
            .thenReturn(responseEntity);
        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        when(objectMapper.readValue(responseEntity.getBody(), CaseDetails.class))
            .thenThrow(new JsonProcessingException("Test Exception") {});

        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response).isNull();

    }

}
