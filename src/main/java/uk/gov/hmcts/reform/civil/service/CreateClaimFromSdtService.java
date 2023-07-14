package uk.gov.hmcts.reform.civil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimMapper;
import uk.gov.hmcts.reform.civil.model.CreateSDTResponse;
import uk.gov.hmcts.reform.civil.modelsdt.CreateClaimSDT;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateClaimFromSdtService {

    private final CreateSDTResponse createSDTResponse;
    private final SubmitCreateClaim submitCreateClaim;

    public ResponseEntity<CreateSDTResponse> buildResponse(CreateClaimSDT createClaimSDT) {

        // TODO Index new field stdRequestId, if it exists, claim is duplicate, otherwise new claim requires new caseData field
        var stdRequestId = "unique";
        if (stdRequestId.equals("Notunique")) {
            var response = createSDTResponse.toBuilder()
                .errorText("201 Request already processed")
                .errorCode("201")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }

        // TODO retrieve PRD properly
        var idamId = "testIdamIDMatchesBulkId";
        if (!idamId.equals(createClaimSDT.getBulkCustomerId())) {
            var response = createSDTResponse.toBuilder()
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
            var response = createSDTResponse.toBuilder()
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
            var response = createSDTResponse.toBuilder()
                .errorText("005 claimant details missing")
                .errorCode("005")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }

        // TODO validate postcode with postcode validation service
        var def1PostCode = createClaimSDT.getDefendant1().getAddress().getPostcode();
        var def2PostCode = createClaimSDT.getDefendant2().getAddress().getPostcode();

        if (def1PostCode.equals("NI postcode")) {
            System.out.println("should not reach here");
            var response = createSDTResponse.toBuilder()
                .errorText("008 First defendant’s postcode is not in England or Wales")
                .errorCode("008")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }

        if (createClaimSDT.getDefendant1().getName().equals(createClaimSDT.getDefendant2().getName())) {

            var response = createSDTResponse.toBuilder()
                .errorText("009 Second defendant cannot have an identical name to the first defendant")
                .errorCode("009")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        }
        // TODO validate postcode with postcode validation service
        if (def2PostCode.equals("NI postcode")) {
            var response = createSDTResponse.toBuilder()
                .errorText("009 Second defendant’s postcode is not in England or Wales' if not")
                .errorCode("009")
                .build();
            return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
        } else {
            return submitCreateClaim.submitClaim(processSdtClaim(createClaimSDT));
        }

    }

    public CreateClaimCCD processSdtClaim(CreateClaimSDT createClaimSDT) {
        CreateClaimMapper createClaimMapper = new CreateClaimMapper();
        createClaimMapper.mappedCreateClaim(createClaimSDT);
        return createClaimMapper.getCreateClaimCCD();
    }

}
