package uk.gov.hmcts.reform.civil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimMapper;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateClaimFromSdtService {

    private final CreateClaimErrorResponse createClaimErrorResponse;
    private final SubmitCreateClaim submitCreateClaim;

    public ResponseEntity<CreateClaimErrorResponse> buildResponse(CreateClaimRequest createClaimRequest) {

        // TODO Index new field stdRequestId, if it exists, claim is duplicate, otherwise new claim requires new caseData field
        var stdRequestId = "unique";
        if (stdRequestId.equals("Notunique")) {
            var response = createClaimErrorResponse.toBuilder()
                .errorText("201 Request already processed")
                .errorCode("201")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }

        // TODO retrieve PRD properly
        var idamId = "testIdamIDMatchesBulkId";
        if (!idamId.equals(createClaimRequest.getBulkCustomerId())) {
            var response = createClaimErrorResponse.toBuilder()
                .errorText("unknown user")
                .errorCode("401")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }

        // TODO check if payment is valid
        var paymentValid = "valid";
        if (paymentValid.equals("notValid")) {
            var response = createClaimErrorResponse.toBuilder()
                .errorText("003 D/D facility not set")
                .errorCode("003")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }

        // TODO Determine if customer is registered as solictor....customerID?
        Boolean customerIsSolicitor = true;
        if (customerIsSolicitor.equals(false)) {
            var response = createClaimErrorResponse.toBuilder()
                .errorText("005 claimant details missing")
                .errorCode("005")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }

        // TODO validate postcode with postcode validation service
        var def1PostCode = createClaimRequest.getDefendant1().getAddress().getPostcode();
        var def2PostCode = createClaimRequest.getDefendant2().getAddress().getPostcode();

        if (def1PostCode.equals("NI postcode")) {
            System.out.println("should not reach here");
            var response = createClaimErrorResponse.toBuilder()
                .errorText("008 First defendant’s postcode is not in England or Wales")
                .errorCode("008")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }

        if (createClaimRequest.getDefendant1().getName().equals(createClaimRequest.getDefendant2().getName())) {

            var response = createClaimErrorResponse.toBuilder()
                .errorText("009 Second defendant cannot have an identical name to the first defendant")
                .errorCode("009")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }
        // TODO validate postcode with postcode validation service
        if (def2PostCode.equals("NI postcode")) {
            var response = createClaimErrorResponse.toBuilder()
                .errorText("009 Second defendant’s postcode is not in England or Wales' if not")
                .errorCode("009")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        } else {
            return submitCreateClaim.submitClaim(processSdtClaim(createClaimRequest));
        }

    }

    public CreateClaimCCD processSdtClaim(CreateClaimRequest createClaimRequest) {
        CreateClaimMapper createClaimMapper = new CreateClaimMapper();
        createClaimMapper.mappedCreateClaim(createClaimRequest);
        return createClaimMapper.getCreateClaimCCD();
    }

}
