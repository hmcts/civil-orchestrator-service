package uk.gov.hmcts.reform.civil.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.civil.model.CreateSDTResponse;
import uk.gov.hmcts.reform.civil.model.SdtErrorResponse;
import uk.gov.hmcts.reform.civil.modelsdt.CreateClaimSDT;
import uk.gov.hmcts.reform.civil.service.CreateClaimFromSdtService;

@Slf4j
@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor
public class CreateClaimSdtController {

    @Autowired
    private CreateClaimFromSdtService createClaimFromSdtService;


    @PostMapping(path = "/createSDTClaim", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create claim SDT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Callback processed."),
        @ApiResponse(responseCode = "400", description = "Bad Request")})
    public ResponseEntity<CreateSDTResponse> createClaimSdt(@RequestBody @Valid CreateClaimSDT createClaimSDT) {

        return createClaimFromSdtService.buildResponse(createClaimSDT);
    }

}

