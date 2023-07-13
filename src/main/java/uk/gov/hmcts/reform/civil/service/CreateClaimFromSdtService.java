package uk.gov.hmcts.reform.civil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.exceptions.ClaimantValidationException;
import uk.gov.hmcts.reform.civil.exceptions.InvalidUserException;
import uk.gov.hmcts.reform.civil.exceptions.PaymentNotFoundException;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimMapper;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateClaimFromSdtService {

    private final SubmitCreateClaim submitCreateClaim;

    public ResponseEntity<CreateClaimErrorResponse> buildResponse(String authorization, CreateClaimRequest createClaimRequest) {

        validateRequestParams(createClaimRequest);
        return submitCreateClaim.submitClaim(processSdtClaim(createClaimRequest));
    }

    private void validateRequestParams(CreateClaimRequest createClaimRequest) {

        var idamId = "testIdamIDMatchesBulkId";
        if (!idamId.equals(createClaimRequest.getBulkCustomerId())) {
            throw new InvalidUserException("Unknown useur");
        }

        // TODO check if payment is valid
        var paymentValid = "valid";
        if (paymentValid.equals("notValid")) {
            throw new PaymentNotFoundException("003 D/D facility not set");
        }

        // TODO Determine if customer is registered as solictor....customerID?
        Boolean customerIsSolicitor = true;
        if (customerIsSolicitor.equals(false)) {
            throw new ClaimantValidationException("005 claimant details missing");
        }

    }

    public CreateClaimCCD processSdtClaim(CreateClaimRequest createClaimRequest) {
        CreateClaimMapper createClaimMapper = new CreateClaimMapper();
        createClaimMapper.mappedCreateClaim(createClaimRequest);
        return createClaimMapper.getCreateClaimCCD();
    }

    public String getSdtRequestId() {
        return "unique";
    }
}
