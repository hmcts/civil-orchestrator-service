package uk.gov.hmcts.reform.civil.modelsdt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@Data
public class CreateClaimSDT {

    // TODO remove bulkCustomerId, not part of payload, believe it is sent as header
    private String bulkCustomerId;
    private String claimantReference;
    private ClaimantType claimant;
    private DefendantType defendant1;
    private DefendantType defendant2;
    private String particulars;
    private Long claimAmount;
    private Boolean reserveRightToClaimInterest;
    private String sotSignature;

    @NotNull
    private String sotSignatureRole;
    private Interest interest;

}
