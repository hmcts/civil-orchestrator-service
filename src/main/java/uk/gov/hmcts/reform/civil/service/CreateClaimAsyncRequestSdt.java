package uk.gov.hmcts.reform.civil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimAsyncRequest;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimAsyncResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateClaimAsyncRequestSdt {

    private final RestTemplate restTemplate;

    public void sendAsyncRequestToSdt(CreateClaimAsyncResponse createClaimAsyncResponse, String SdtRequestId) {
        String url = "https://5067dd19-05af-4a3f-a9f3-913af72b22fb.mock.pstmn.io/CMCfeedback";
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("SdtRequestId", SdtRequestId);
        // Populate request body
        CreateClaimAsyncRequest requestBody = CreateClaimAsyncRequest.builder()
            .issueDate(createClaimAsyncResponse.getIssueDate())
            .fee(createClaimAsyncResponse.getFee())
            .build();
        // Create HttpEntity
        HttpEntity<CreateClaimAsyncRequest> httpEntity = new HttpEntity<>(requestBody, headers);
        // Make POST request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        log.info("Response status code: " + response.getStatusCode());

    }
}
