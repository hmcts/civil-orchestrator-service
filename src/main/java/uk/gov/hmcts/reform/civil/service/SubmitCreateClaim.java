package uk.gov.hmcts.reform.civil.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
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
    private String authorisation = "Bearer eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiYi9PNk92VnYxK3krV2dySDVVaTlXVGlvTHQwPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJhdXRoX2xldmVsIjowLCJhdWRpdFRyYWNraW5nSWQiOiJkZTAwMjMzYi02MDJiLTQyZDMtOTExZS1hM2U1YzFlNDhjZWIiLCJpc3MiOiJodHRwOi8vZnItYW06ODA4MC9vcGVuYW0vb2F1dGgyL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiMGExMTVjMmMtYjNmOS00YmE4LThhMmQtNDMwM2ViNGI2Mzg5IiwiYXVkIjoiaG1jdHMiLCJuYmYiOjE2ODc0NDI2MTAsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJyb2xlcyJdLCJhdXRoX3RpbWUiOjE2ODc0NDI2MTAsInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNjg3NDcxNDEwLCJpYXQiOjE2ODc0NDI2MTAsImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJkZmZhMWMzZS0wYWU3LTQ1YjctYWNjNS05ODRkYzBjYzIyMmYifQ.ZGdSIbA-MWl1wQE8mzL4POUWkYZIzAqghEbl_3ucrfmOP4TOB4ArrLZ01yJ9f86uYvIcFPNFp1ZGIs6Ly-vi7IHo4iueaAyWWKBrdle57BLiQc-gbcN3-1GiSegKn4oxS_fkRt2iPNq_2ekM-ibwAyUNi6a5zHG_BC3pRP5fAkAblt0DIB1NmMI2BpQt6TEH7FcuxxhvCi07pTNhazALDQkbHZU_IlXIQYSkSZc-smwlldpn96MF2IHarkQ1JGVHiQjyTyFEAqWUAfGhpL6WC9M9-U3MYfxOhO-tbvAD-ie8HnCQSoi1MQlBONhNErP-utXuAxj5x0EH6FciGihnCQ";
    private String serviceAuthorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaXZpbF9zZXJ2aWNlIiwiZXhwIjoxNjg3NDU3MDA5fQ.sGuzGMy9Z9GtVq5WXFOh_KvrhGjyZxOs_MBB40nRRCWzqQuETo7z6DzZKIydlDttBtaTwaubM2BLhWzTEFNXrQ";
    private String eventToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJoZmwzZ2xkNHFnaGZxZ2g1a2k3YTBpbmU1ZCIsInN1YiI6IjJkYTMxYWYzLWY1YzktNDExYi05ZWEzLWI1ZjgwYzk5NGZkMSIsImlhdCI6MTY4NzQ0MjYxNCwiZXZlbnQtaWQiOiJDUkVBVEVfQ0xBSU1fU1BFQyIsImNhc2UtdHlwZS1pZCI6IkNJVklMIiwianVyaXNkaWN0aW9uLWlkIjoiQ0lWSUwiLCJjYXNlLXZlcnNpb24iOiI0NDEzNmZhMzU1YjM2NzhhMTE0NmFkMTZmN2U4NjQ5ZTk0ZmI0ZmMyMWZlNzdlODMxMGMwNjBmNjFjYWFmZjhhIn0.TdtJ82eHilgzvexomR1nMeCFCw5FTRbNmc3MvamJG9M";

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
            if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(401))) {
                var response = createSDTResponse.toBuilder()
                    .errorText("401: UNAUTHORIZED")
                    .errorCode("401")
                    .build();
                return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST);
            }
            if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(422))) {
                var validationResponse = createSDTResponse.toBuilder()
                    .errorText("422: Case validation error: " + e)
                    .errorCode("422")
                    .build();
                return new ResponseEntity<>(
                    validationResponse,
                    HttpStatus.BAD_REQUEST);
            }
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
