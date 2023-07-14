package uk.gov.hmcts.reform.civil.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.civil.model.CreateSDTResponse;
import uk.gov.hmcts.reform.civil.modelsdt.CreateClaimSDT;
import uk.gov.hmcts.reform.civil.service.CreateClaimFromSdtService;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CreateClaimSdtController {

    private final CreateClaimFromSdtService createClaimFromSdtService;

    @PostMapping(path = "/createSDTClaim", consumes = APPLICATION_XML_VALUE)
    @Operation(summary = "Create claim SDT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Callback processed."),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
    public ResponseEntity<CreateSDTResponse> createClaimSdt(@RequestBody CreateClaimSDT createClaimSDT) {

        return createClaimFromSdtService.buildResponse(createClaimSDT);
    }
}

