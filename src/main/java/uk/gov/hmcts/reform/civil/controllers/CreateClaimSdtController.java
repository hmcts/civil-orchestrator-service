package uk.gov.hmcts.reform.civil.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimMapper;
import uk.gov.hmcts.reform.civil.modelsdt.CreateClaimSDT;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CreateClaimSdtController {

    @PutMapping(path = "/SDTCreateClaim", produces = APPLICATION_XML_VALUE, consumes = APPLICATION_XML_VALUE)
    @Operation(summary = "Create claim SDT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Callback processed."),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
    public void createClaimSdt(@RequestBody CreateClaimSDT createClaimSDT) {
        try {
            CreateClaimMapper createClaimMapper = new CreateClaimMapper();
            createClaimMapper.mappedCreateClaim(createClaimSDT);
        } catch (Exception ex) {
            log.error("Create claim SDT callback failed", ex);
        }
    }
}

