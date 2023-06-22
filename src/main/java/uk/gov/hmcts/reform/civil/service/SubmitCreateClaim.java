package uk.gov.hmcts.reform.civil.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.model.CreateSDTResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitCreateClaim {

    private final CreateSDTResponse createSDTResponse;
    private String authorisation = "";
    private String serviceAuthorization = "";
    private String eventToken = "";

    public ResponseEntity<CreateSDTResponse> submitClaim(CreateClaimCCD createClaimCCD) {

        JSONObject caseDataJsonObj = new JSONObject(createClaimCCD);

        // workaround, MoneyGBP validation error
        caseDataJsonObj.getJSONObject("claimFee").put("calculatedAmountInPence", "10099");
        // workaround, YesOrNo validation error
        caseDataJsonObj.getJSONObject("applicantSolicitor1CheckEmail").put("correct", "Yes");
        caseDataJsonObj.remove("adddRespondent2");
        caseDataJsonObj.put("addRespondent2", "Yes");
        caseDataJsonObj.put("claimInterest", "Yes");

        // workaround, validation expects orgPolicyCaseAssignedRole to be capital O OrgPolicyCaseAssignedRole, as with other fields
        caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").remove("orgPolicyCaseAssignedRole");
        caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").remove("organisation");
        caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").put("OrgPolicyCaseAssignedRole", "[APPLICANTSOLICITORONE]");
        // caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").put("OrgPolicyReference", "null");
        // caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").put("PrepopulateToUsersOrganisation", "No");
        caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").put("Organisation", "");
        JSONObject organisation = new JSONObject();
        // organisation.put("OrganisationName", "Civil - Organisation 1");
        organisation.put("OrganisationID", "Q1KOKP2");
        caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").put("Organisation", organisation);
        System.out.println("the updated json obj" + caseDataJsonObj);

        String url = "http://localhost:4452/caseworkers/2da31af3-f5c9-411b-9ea3-b5f80c994fd1/jurisdictions/CIVIL/case-types/CIVIL/cases";

        // Create HttpHeaders
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization", "" + authorisation + "");
        header.set("ServiceAuthorization", "" + serviceAuthorization + "");

        // Set body
        var caseData =  caseDataJsonObj.toString();
        String requestBody = "{\"data\":" + caseData + ","
            + "\"event\": {\"id\": \"CREATE_CLAIM_SPEC\"},"
            + "\"event_data\": {},"
            + "\"event_token\": \"" + eventToken + "\"}";

        //  HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, header);
        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        // POST request
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            // Process the response
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Request was a success");
                System.out.println("Response Body: " + response.getBody());
                return getClaimNumber(response);
            }
        } catch (HttpClientErrorException e) {
            System.out.println("error message of " + e);
            log.error(e.getMessage());
            var response = createSDTResponse.toBuilder()
                .errorText("422: Case validation error: " + e)
                .errorCode("422")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<CreateSDTResponse> getClaimNumber(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            CaseDetails caseDetails;
            caseDetails = objectMapper.readValue(response.getBody(), CaseDetails.class);
            var responseNum = createSDTResponse.toBuilder()
                .claimNumber(caseDetails.getId().toString())
                .build();
            return new ResponseEntity<>(
                responseNum,
                HttpStatus.OK);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
