package uk.gov.hmcts.reform.civil.modelsdt;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder(toBuilder = true)
@Value
@AllArgsConstructor
public class CreateClaimSDT {

    // TODO remove bulkCustomerId, not part of payload, believe it is sent as header
    private final String bulkCustomerId;
    private final String claimantReference;
    private final ClaimantType claimant;
    private final DefendantType defendant1;
    private final DefendantType defendant2;
    private final String particulars;
    private final Long claimAmount;
    private final Boolean reserveRightToClaimInterest;
    private final String sotSignature;

    @NotNull
    private final String sotSignatureRole;
    private final Interest interest;

}
