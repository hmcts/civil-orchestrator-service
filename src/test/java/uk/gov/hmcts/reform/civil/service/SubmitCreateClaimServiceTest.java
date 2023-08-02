package uk.gov.hmcts.reform.civil.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.civil.config.CreateClaimConfiguration;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.prd.model.Organisation;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponse;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubmitCreateClaimServiceTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    OrganisationService organisationService;
    @Mock
    UserService userService;
    @Mock
    CreateClaimConfiguration createClaimConfiguration;
    @Mock
    private ObjectMapper objectMapper;

    SubmitCreateClaimService submitCreateClaimService;

    private String authorization = "Bearer 123";
    private String caseDetailsJson;


    @BeforeEach
    void setup() throws JsonProcessingException {
        submitCreateClaimService = new SubmitCreateClaimService(restTemplate, organisationService,
                                                                userService, createClaimConfiguration);

        given(userService.getUserInfo(anyString())).willReturn(UserInfo.builder().uid("uid").build());

        given(userService.getUserDetails(authorization)).willReturn(UserDetails.builder().id("userId").email("Test@test.com").build());

        given(organisationService.findOrganisation(anyString()))
            .willReturn(Optional.of(Organisation.builder().organisationIdentifier("Test Org id").build()));

        given(createClaimConfiguration.getUrl()).willReturn("testUrl");

        CaseDetails responseCaseDetails = CaseDetails.builder().build();
        responseCaseDetails.setData(new HashMap<>());
        String legacyCaseReference = "CIV12345";
        responseCaseDetails.getData().put("legacyCaseReference", legacyCaseReference);

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        caseDetailsJson = objectMapper.writeValueAsString(responseCaseDetails);
    }

    @Test
    void shouldCreateClaimSuccessFully_whenInvoked() throws JsonProcessingException {
        // Given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(caseDetailsJson, HttpStatus.OK);
        given(restTemplate.exchange(eq("testUrl" + "uid"), eq(HttpMethod.POST), any(), eq(String.class)))
            .willReturn(responseEntity);

        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    void shouldNotCreateClaimSuccessFully_whenInvokedAndUnauthorised() {
        // Given
        given(restTemplate.exchange(eq("testUrl" + "uid"), eq(HttpMethod.POST), any(), eq(String.class)))
            .willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

    @Test
    void shouldNotCreateClaimSuccessFully_whenInvokedAndInvalidData() {
        // Given
        given(restTemplate.exchange(eq("testUrl" + "uid"), eq(HttpMethod.POST), any(), eq(String.class)))
            .willThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));

        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @Test
    void shouldNotCreateClaimSuccessFully_whenInvokedAndForbidden() {
        // Given
        given(restTemplate.exchange(eq("testUrl" + "uid"), eq(HttpMethod.POST), any(), eq(String.class)))
            .willThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        CreateClaimCCD createClaimCCD = CreateClaimCCD.builder().build();
        // When
        ResponseEntity<CreateClaimResponse> response = submitCreateClaimService.submitClaim(authorization, createClaimCCD);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

}
