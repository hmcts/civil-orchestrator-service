package uk.gov.hmcts.reform.civil.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.civil.model.casedata.IdamUserDetails;
import uk.gov.hmcts.reform.civil.model.casedata.OrganisationPolicy;
import uk.gov.hmcts.reform.civil.model.prd.CivilServiceApi;
import uk.gov.hmcts.reform.civil.model.prd.model.Organisation;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponse;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponseBody;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimSyncResponse;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitCreateClaimService {

    private final OrganisationService organisationService;
    private final CivilServiceApi civilServiceApi;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public ResponseEntity<CreateClaimResponse> submitClaim(String authorization, CreateClaimCCD createClaimCCD) {

        // Organisation policy not sent by SDT,  built manually via PRD organisationApi.findUserOrganisation
        // to retrieve org id
        // TODO: PRD adding new endpoint for bulk claims to retrieve IDs using a customer id
        createClaimCCD.setApplicant1OrganisationPolicy(populateApplicant1OrgPolicy(authorization));
        // populate ApplicantSolicitor1UserDetails/idamUserDetails
        UserDetails userDetails = userService.getUserDetails(authorization);
        // TODO New PRD endpoint will have email address info, use that when implemented
        String userId = userService.getUserInfo(authorization).getUid();
        createClaimCCD.setApplicantSolicitor1UserDetails(IdamUserDetails.builder()
                                                             .id(userId)
                                                             .email(userDetails.getEmail())
                                                             .build());

        CreateClaimResponseBody requestBody = new CreateClaimResponseBody(createClaimCCD, "CREATE_CLAIM_SPEC");
        try {
            ResponseEntity<String> response = civilServiceApi.caseworkerSubmitEvent(userId,authorization,requestBody);
            if (response.getStatusCode().is2xxSuccessful()) {
                return getBulkCaseManClaimNumber(response);
            }
        } catch (HttpClientErrorException e) {
            CreateClaimErrorResponse errorResponse = CreateClaimErrorResponse.builder().build();
            log.error(e.getMessage());
            if (e.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                errorResponse.toBuilder().errorText("401: UNAUTHORIZED")
                       .errorCode("401").build();
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }
            if (e.getStatusCode().isSameCodeAs(HttpStatus.UNPROCESSABLE_ENTITY)) {
                errorResponse.toBuilder().errorText("422: Case validation error: " + e)
                    .errorCode("422").build();
                return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (e.getStatusCode().isSameCodeAs(HttpStatus.FORBIDDEN)) {
                errorResponse.toBuilder().errorText("403: Forbidden access Denied: " + e)
                    .errorCode("403").build();
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
        }
        return null;
    }

    public ResponseEntity<CreateClaimResponse> getBulkCaseManClaimNumber(ResponseEntity<String> response) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            CaseDetails caseDetails = objectMapper.readValue(response.getBody(), CaseDetails.class);
            String legacyCaseReference = caseDetails.getData().get("legacyCaseReference").toString();
            CreateClaimSyncResponse createClaimSyncResponse = CreateClaimSyncResponse.builder()
                .claimNumber(legacyCaseReference).build();
            return new ResponseEntity<>(createClaimSyncResponse, HttpStatus.CREATED);
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
