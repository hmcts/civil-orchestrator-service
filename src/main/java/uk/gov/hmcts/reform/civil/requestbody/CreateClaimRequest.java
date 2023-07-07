package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimRequest {

    // TODO remove bulkCustomerId, not part of payload, believe it is sent as header
    @Pattern(regexp = "[1-9]\\d{7}", message = "Bulk customer Id is in wrong format")
    private String bulkCustomerId;
    @NotNull
    private String claimantReference;
    private ClaimantType claimant;
    private DefendantType defendant1;
    private DefendantType defendant2;
    @Max(45) @NotNull
    private String particulars;
    @Min(0) @Max(99999)
    private Long claimAmount;
    private Boolean reserveRightToClaimInterest;
    @NotNull
    private String sotSignature;
    private final String sotSignatureRole = "bulk issuer role";
    private Interest interest;

}
