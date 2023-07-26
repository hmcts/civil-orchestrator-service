package uk.gov.hmcts.reform.civil.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.civil.exceptions.BadRequestException;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;
import uk.gov.hmcts.reform.civil.service.CreateClaimFromSdtService;

@Tag(name = "Create Claim From SDT Controller")
@Slf4j
@RestController
@RequiredArgsConstructor
public class CreateClaimSdtController {

    private final CreateClaimFromSdtService createClaimFromSdtService;

    @PostMapping(path = "/createSDTClaim", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create claim from SDT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Callback processed."),
        @ApiResponse(responseCode = "400", description = "Bad Request")})

    public ResponseEntity<CreateClaimErrorResponse> createClaimSdt(@Valid @RequestBody CreateClaimRequest createClaimRequest,
                                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                                                   @RequestHeader("SdtRequestId") String sdtRequestId) {
        validateSdtRequestId(sdtRequestId);
        return createClaimFromSdtService.buildResponse(authorization,createClaimRequest, sdtRequestId);
    }

    private void validateSdtRequestId(String sdtRequestId) {
        String sdtRequestIdFromCcd = createClaimFromSdtService.getSdtRequestId();
        if (sdtRequestIdFromCcd.equalsIgnoreCase(sdtRequestId)) {
            throw new BadRequestException("Create claim request already processed");
        }
    }
}

