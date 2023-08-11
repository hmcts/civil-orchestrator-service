package uk.gov.hmcts.reform.civil.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimAsyncResponse;
import uk.gov.hmcts.reform.civil.service.CreateClaimAsyncRequestSdt;

@Tag(name = "Create Claim Async Response Controller")
@Slf4j
@RestController
@RequiredArgsConstructor
public class CreateClaimAsyncResponseController {

    String GOOD_REQUEST = "Successfully received async request";
    private final CreateClaimAsyncRequestSdt createClaimAsyncRequestSdt;

    @PostMapping(path = "/createClaimAsyncResponse", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Claim Async Response")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Async callback processed."),
        @ApiResponse(responseCode = "400", description = "Bad Request")})

    public ResponseEntity<String> createClaimSdt(@Valid @RequestBody CreateClaimAsyncResponse createClaimAsyncResponse,
                                                        @RequestHeader("SdtRequestId") String sdtRequestId) throws Exception {
        try {
            log.info("receiving async request from civil-service");
            createClaimAsyncRequestSdt.sendAsyncRequestToSdt(createClaimAsyncResponse, sdtRequestId);
            log.info("Request sent to SDT");
            return new ResponseEntity<>(GOOD_REQUEST, HttpStatus.OK);
        } catch (Exception ex) {
            throw new Exception("Unsuccessful:  " + ex);
        }
    }

}

