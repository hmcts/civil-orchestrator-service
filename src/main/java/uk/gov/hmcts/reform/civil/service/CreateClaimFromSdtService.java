package uk.gov.hmcts.reform.civil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.exceptions.ClaimantValidationException;
import uk.gov.hmcts.reform.civil.exceptions.InvalidUserException;
import uk.gov.hmcts.reform.civil.exceptions.PaymentNotFoundException;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimMapperInterface;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateClaimFromSdtService {

    private final SubmitCreateClaimService submitCreateClaimService;
    private CreateClaimCCD createClaimCCD;

    public ResponseEntity<CreateClaimResponse> buildResponse(String authorization, CreateClaimRequest createClaimRequest,
                                                             String sdtRequestId) {
        createClaimRequest.setSdtRequestId(sdtRequestId);
        validateRequestParams(createClaimRequest);
        return submitCreateClaimService.submitClaim(authorization, processSdtClaim(createClaimRequest));
    }

    private void validateRequestParams(CreateClaimRequest createClaimRequest) {

        var idamId = "12345678";
        if (!idamId.equals(createClaimRequest.getBulkCustomerId())) {
            throw new InvalidUserException("Unknown useur");
        }

        // TODO check if payment is valid
        var paymentValid = "valid";
        if (paymentValid.equals(createClaimRequest.getClaimantReference())) {
            throw new PaymentNotFoundException("003 D/D facility not set");
        }

        // TODO Determine if customer is registered as solictor....customerID?
        Boolean customerIsSolicitor = true;
        String customerName = "claimant";
        if (customerName.equals(createClaimRequest.getClaimant().getName())) {
            customerIsSolicitor = false;
            throw new ClaimantValidationException("005 claimant details missing");
        }
    }

    public CreateClaimCCD processSdtClaim(CreateClaimRequest createClaimRequest) {
        createClaimCCD = CreateClaimMapperInterface.INSTANCE.claimToDto(createClaimRequest);

        return createClaimCCD;
    }

    // TODO search query, for stdRef if stdRequestId exists, request is duplicate
    public String getSdtRequestId() {
        return "unique";
    }
}
