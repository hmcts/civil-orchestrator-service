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
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitCreateClaim {

    private final CreateClaimErrorResponse createClaimErrorResponse;
    private String authorisation = "";
    private String serviceAuthorization = "";
    private String eventToken = "";
    private String userID = "";
    private String jsonCaseData;
    private final ObjectMapper objectMapper;

    public ResponseEntity<CreateClaimErrorResponse> submitClaim(CreateClaimCCD createClaimCCD) {

        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            jsonCaseData = objectMapper.writeValueAsString(createClaimCCD);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String url = "http://localhost:4452/caseworkers/" + userID + "/jurisdictions/CIVIL/case-types/CIVIL/cases";

        // Create HttpHeaders
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization", "" + authorisation + "");
        header.set("ServiceAuthorization", "" + serviceAuthorization + "");

        // Set body
        String requestBody = "{\"data\":" + jsonCaseData + ","
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

    public ResponseEntity<CreateClaimErrorResponse> getClaimNumber(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            CaseDetails caseDetails;
            caseDetails = objectMapper.readValue(response.getBody(), CaseDetails.class);
            var responseNum = createClaimErrorResponse.toBuilder()
                .build();
            return new ResponseEntity<>(
                responseNum,
                HttpStatus.CREATED);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
