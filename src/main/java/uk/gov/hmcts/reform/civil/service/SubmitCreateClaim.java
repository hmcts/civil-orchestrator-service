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
    private String authorisation = "Bearer eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiYi9PNk92VnYxK3krV2dySDVVaTlXVGlvTHQwPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJhdXRoX2xldmVsIjowLCJhdWRpdFRyYWNraW5nSWQiOiI3YmFkNDA4My1kZjg0LTQwZWItODUwNi01NmRjM2UzN2RmZmIiLCJpc3MiOiJodHRwOi8vZnItYW06ODA4MC9vcGVuYW0vb2F1dGgyL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiZjA5ZWFlMGEtZGQ5OS00ZDIyLWJiNzAtMjE0YTM5Y2Y4Y2JjIiwiYXVkIjoiaG1jdHMiLCJuYmYiOjE2ODc0MjU0NTksImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJyb2xlcyJdLCJhdXRoX3RpbWUiOjE2ODc0MjU0NTksInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNjg3NDU0MjU5LCJpYXQiOjE2ODc0MjU0NTksImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJiMDU5MTNjYy02YWNlLTQ4YzctODRkNy02NWMzYzc2N2Q0OTkifQ.EpuDWqszVmHijIDv60tDwvyAIEINWUibdWYFy2t49bSR5n4SR02-s4KjRnooLndT9JBlp7oXl1Ds4HlTzlBmZtt9XYwFwomQYE1P3IJR_sxPsGlNBWsqtSLS9XzM4TiNYHyuXnAKwHyMFHf1boiYe1XTRfvdNzZ09pq3Rw055DzkN7DuzElsPi3JOO8XPJAYTsx6vV-pbWgl4diRJxSGoCvOdS3fnGVDvuukDhgXanXjJNKAzDpLYprdtvc0YpnSDE4KenjQku6iZrwLUh53LaLojtkKY0jSZnnZMHXn7-DFWqT4xq3EQa2KbMHQOU6WJ97jzMXSgGxbyCkdkpP0IA";
    private String serviceAuthorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaXZpbF9zZXJ2aWNlIiwiZXhwIjoxNjg3NDM5Nzc2fQ.jTbpNXDkTn9DcWYSSDfQT4Nk9BLMyOOJ9TRChhq4yFBiIGoOSDDeEIYMfGtsuPv4qKsWVOvpDrJhqBXlMn4oSA";
    private String eventToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3YnVzYThrdjQ0MjJmcmZta3IyZ2liNXFvcSIsInN1YiI6IjJkYTMxYWYzLWY1YzktNDExYi05ZWEzLWI1ZjgwYzk5NGZkMSIsImlhdCI6MTY4NzQyNTM5NCwiZXZlbnQtaWQiOiJDUkVBVEVfQ0xBSU1fU1BFQyIsImNhc2UtdHlwZS1pZCI6IkNJVklMIiwianVyaXNkaWN0aW9uLWlkIjoiQ0lWSUwiLCJjYXNlLXZlcnNpb24iOiI0NDEzNmZhMzU1YjM2NzhhMTE0NmFkMTZmN2U4NjQ5ZTk0ZmI0ZmMyMWZlNzdlODMxMGMwNjBmNjFjYWFmZjhhIn0.mNweAXKcb1byOeYXYSqXJrD39SKIMLG3AYgpgLCIS3s";

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
//        caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").put("OrgPolicyReference", "null");
//        caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").put("PrepopulateToUsersOrganisation", "No");
        caseDataJsonObj.getJSONObject("applicant1OrganisationPolicy").put("Organisation", "");
        JSONObject organisation = new JSONObject();
//        organisation.put("OrganisationName", "Civil - Organisation 1");
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
        String requestBody = "{\"data\":" + caseData +","
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
            var aResponse = createSDTResponse.toBuilder()
                .claimNumber(caseDetails.getId().toString())
                .build();
            return new ResponseEntity<>(
                aResponse,
                HttpStatus.OK);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
