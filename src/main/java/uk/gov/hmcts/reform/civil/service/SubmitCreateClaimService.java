package uk.gov.hmcts.reform.civil.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.civil.config.CreateClaimConfiguration;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.model.casedata.IdamUserDetails;
import uk.gov.hmcts.reform.civil.model.casedata.OrganisationPolicy;
import uk.gov.hmcts.reform.civil.prd.model.Organisation;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponse;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponseBody;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimSuccessfulResponse;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitCreateClaimService {

    private final RestTemplate restTemplate;
    private final OrganisationService organisationService;
    private final UserService userService;
    private final CreateClaimConfiguration createClaimConfiguration;

    public ResponseEntity<CreateClaimResponse> submitClaim(String authorization, CreateClaimCCD createClaimCCD) {

        // Organisation policy not sent by SDT,  built manually via PRD organisationApi.findUserOrganisation
        // to retrieve org id
        // TODO: PRD adding new endpoint for bulk claims to retrieve IDs using a customer id
        createClaimCCD.setApplicant1OrganisationPolicy(populateApplicant1OrgPolicy(authorization));
        //retrieve UserId
        String userId = userService.getUserInfo(authorization).getUid();
        // populate ApplicantSolicitor1UserDetails/idamUserDetails
        UserDetails userDetails = userService.getUserDetails(authorization);
        // TODO New PRD endpoint will have email address info, use that when implemented
        createClaimCCD.setApplicantSolicitor1UserDetails(IdamUserDetails.builder()
                                                             .id(userId)
                                                             .email(userDetails.getEmail())
                                                             .build());
        // Create HttpHeaders
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(HttpHeaders.AUTHORIZATION, authorization);
        // Set body
        CreateClaimResponseBody responseBody = new CreateClaimResponseBody(createClaimCCD, "CREATE_CLAIM_SPEC");
        //  HttpEntity
        HttpEntity<CreateClaimResponseBody> requestEntity = new HttpEntity<>(responseBody, header);
        // POST request
        try {
            ResponseEntity<String> response = restTemplate.exchange(createClaimConfiguration.getUrl() + userId,
                                                                    HttpMethod.POST, requestEntity, String.class
            );
            // Process the response
            if (response.getStatusCode().is2xxSuccessful()) {
                return getBulkCaseManClaimNumber(response);
            }
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            if (e.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                var response = new CreateClaimErrorResponse();
                response.setErrorText("401: UNAUTHORIZED");
                response.setErrorCode("401");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            if (e.getStatusCode().isSameCodeAs(HttpStatus.UNPROCESSABLE_ENTITY)) {
                var validationResponse = new CreateClaimErrorResponse();
                validationResponse.setErrorText("422: Case validation error: " + e);
                validationResponse.setErrorCode("422");
                return new ResponseEntity<>(validationResponse, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (e.getStatusCode().isSameCodeAs(HttpStatus.FORBIDDEN)) {
                var forbiddenResponse = new CreateClaimErrorResponse();
                forbiddenResponse.setErrorText("403: Forbidden access Denied: " + e);
                forbiddenResponse.setErrorCode("403");
                return new ResponseEntity<>(forbiddenResponse, HttpStatus.FORBIDDEN);
            }
        }
        return null;
    }

    public ResponseEntity<CreateClaimResponse> getBulkCaseManClaimNumber(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            CaseDetails caseDetails;
            caseDetails = objectMapper.readValue(response.getBody(), CaseDetails.class);
            var responseNum = new CreateClaimSuccessfulResponse();
            // TODO Bulk claims require new caseman number to be generated, similar to legacyCaseReference https://tools.hmcts.net/jira/browse/CIV-4463
            responseNum.setClaimNumber(caseDetails.getData().get("legacyCaseReference").toString());
            return new ResponseEntity<>(responseNum, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public OrganisationPolicy populateApplicant1OrgPolicy(String authorization) {
        Optional<Organisation> organisation = organisationService.findOrganisation(authorization);
        OrganisationPolicy organisationPolicy = new OrganisationPolicy();
        organisationPolicy.setOrgPolicyCaseAssignedRole("[APPLICANTSOLICITORONE]");
        organisationPolicy.setOrgPolicyReference(null);
        organisationPolicy.setOrganisation(uk.gov.hmcts.reform.civil.model.casedata.Organisation.builder()
                                               .organisationID(organisation.get().getOrganisationIdentifier()).build());

        return organisationPolicy;
    }

}
