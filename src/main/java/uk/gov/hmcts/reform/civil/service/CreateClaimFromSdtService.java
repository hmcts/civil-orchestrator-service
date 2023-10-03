package uk.gov.hmcts.reform.civil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.exceptions.ApplicationException;
import uk.gov.hmcts.reform.civil.exceptions.ErrorDetails;
import uk.gov.hmcts.reform.civil.mappings.CreateClaimMapperInterface;
import uk.gov.hmcts.reform.civil.model.prd.CivilServiceApi;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimCCD;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateClaimFromSdtService {

    private final SubmitCreateClaimService submitCreateClaimService;
    private final CivilServiceApi civilServiceApi;
    private final UserService userService;

    public ResponseEntity<CreateClaimResponse> buildResponse(String authorization, CreateClaimRequest createClaimRequest,
                                                             String sdtRequestId) {
        createClaimRequest.setSdtRequestId(sdtRequestId);
        validateRequestParams(createClaimRequest,authorization);
        return submitCreateClaimService.submitClaim(authorization, processSdtClaim(createClaimRequest));
    }

    private void validateRequestParams(CreateClaimRequest createClaimRequest, String authorization) {

        if (createClaimRequest.getDefendant1() != null
            && !civilServiceApi.validatePostCode(authorization, createClaimRequest.getDefendant1().getAddress().getPostcode()).isEmpty()) {
            throw new ApplicationException(ErrorDetails.INVALID_DEFENDANT1_POSTCODE, HttpStatus.BAD_REQUEST);
        }
        if (createClaimRequest.getDefendant2() != null
            && !civilServiceApi.validatePostCode(authorization, createClaimRequest.getDefendant2().getAddress().getPostcode()).isEmpty()) {
            throw new ApplicationException(ErrorDetails.INVALID_DEFENDANT2_POSTCODE, HttpStatus.BAD_REQUEST);
        }

        // TODO use headers to check against prd endpoint
        var idamId = "12345678";
        if (!idamId.equals(createClaimRequest.getBulkCustomerId())) {
            throw new ApplicationException(ErrorDetails.UNKNOWN_USER, HttpStatus.BAD_REQUEST);
        }

        // TODO check if payment is valid
        var paymentValid = "valid";
        if (paymentValid.equals(createClaimRequest.getClaimantReference())) {
            throw new ApplicationException(ErrorDetails.INVALID_PAYMENT, HttpStatus.BAD_REQUEST);
        }

        // TODO Determine if customer is registered as solictor....customerID?
        Boolean customerIsSolicitor = true;
        String customerName = "claimant";
        if (customerName.equals(createClaimRequest.getClaimant().getName())) {
            customerIsSolicitor = false;
            throw new ApplicationException(ErrorDetails.INVALID_CLAIMANT_DETAILS, HttpStatus.BAD_REQUEST);
        }

    }

    public CreateClaimCCD processSdtClaim(CreateClaimRequest createClaimRequest) {
        return CreateClaimMapperInterface.INSTANCE.claimToDto(createClaimRequest);
    }

    // TODO search query, for stdRef if stdRequestId exists, request is duplicate
    public Boolean validateSdtRequest(String authorization, String sdtRequestIdFromSdt) {
        String userId = userService.getUserInfo(authorization).getUid();
        return civilServiceApi.searchCaseForCaseworker(
            authorization,
            userId,
            sdtRequestIdFromSdt
        );
    }
}
