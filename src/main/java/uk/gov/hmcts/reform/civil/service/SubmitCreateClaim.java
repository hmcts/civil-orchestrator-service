package uk.gov.hmcts.reform.civil.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.civil.config.CreateClaimConfiguration;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.prd.model.Organisation;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;
import uk.gov.hmcts.reform.civil.responsebody.MockOrg;
import uk.gov.hmcts.reform.civil.responsebody.MockOrgPolicy;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitCreateClaim {

    private final CreateClaimErrorResponse createClaimErrorResponse;
    private String jsonCaseData;
    private final ObjectMapper objectMapper;
    private final OrganisationService organisationService;
    private final UserService userService;
    private final CreateClaimConfiguration createClaimConfiguration;

    public ResponseEntity<CreateClaimErrorResponse> submitClaim(String authorization, CreateClaimCCD createClaimCCD) {

        // Organisation policy not sent by SDT,  built manually via PRD organisationApi.findUserOrganisation
        // to retrieve org id
        // TODO: PRD adding new endpoint for bulk claims to retrieve IDs using a customer id
        createClaimCCD.setApplicant1OrganisationPolicy(populateApplicant1OrgPolicy(authorization));

        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            jsonCaseData = objectMapper.writeValueAsString(createClaimCCD);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //retrieve UserId
        String userId = userService.getUserInfo(authorization).getUid();
        // Create HttpHeaders
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(HttpHeaders.AUTHORIZATION, authorization);
        // Set body
        String requestBody = "{\"data\":" + jsonCaseData + "," + "\"event\": \"CREATE_CLAIM_SPEC\"}";
        //  HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, header);
        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        // POST request
        try {
            ResponseEntity<String> response = restTemplate
                .exchange(createClaimConfiguration.getUrl() + userId, HttpMethod.POST, requestEntity, String.class);
            // Process the response
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Request was a success");
                System.out.println("Response Body: " + response.getBody());
                return getBulkCaseManClaimNumber(response);
            }
        } catch (HttpClientErrorException e) {
            System.out.println("error message of " + e);
            log.error(e.getMessage());
            if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(401))) {
                var response = createClaimErrorResponse.toBuilder()
                    .errorText("401: UNAUTHORIZED")
                    .errorCode("401")
                    .build();
                return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST);
            }
            if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(422))) {
                var validationResponse = createClaimErrorResponse.toBuilder()
                    .errorText("422: Case validation error: " + e)
                    .errorCode("422")
                    .build();
                return new ResponseEntity<>(
                    validationResponse,
                    HttpStatus.BAD_REQUEST);
            }
            if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(403))) {
                var validationResponse = createClaimErrorResponse.toBuilder()
                    .errorText("403: Forbidden access Denied: " + e)
                    .errorCode("403")
                    .build();
                return new ResponseEntity<>(
                    validationResponse,
                    HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    public ResponseEntity<CreateClaimErrorResponse> getBulkCaseManClaimNumber(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            CaseDetails caseDetails;
            caseDetails = objectMapper.readValue(response.getBody(), CaseDetails.class);
            var responseNum = createClaimErrorResponse.toBuilder()
                // TODO Bulk claims require new casman number to be generated, similar to legacyCaseReference https://tools.hmcts.net/jira/browse/CIV-4463
                .claimNumber(caseDetails.getData().get("legacyCaseReference").toString())
                .build();
            return new ResponseEntity<>(
                responseNum,
                HttpStatus.OK);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public MockOrgPolicy populateApplicant1OrgPolicy(String authorization) {
        Optional<Organisation> organisation = organisationService.findOrganisation(authorization);
        MockOrgPolicy organisationPolicy = new MockOrgPolicy();
        organisationPolicy.setOrgPolicyCaseAssignedRole("[APPLICANTSOLICITORONE]");
        organisationPolicy.setOrgPolicyReference(null);
        organisationPolicy.setOrganisation(MockOrg.builder()
                                               .organisationID(organisation.get().getOrganisationIdentifier()).build());

        return organisationPolicy;
    }

}
