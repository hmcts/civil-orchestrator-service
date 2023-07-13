package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.reform.civil.customvalidator.ValidateFields;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidateFields(
    field = "defendant1.name",
    parentField = "defendant2",
    fieldMatch = "defendant2.name",
    message = "Second defendant cannot have an identical name to the first defendant"
)
public class CreateClaimRequest {

    @NotNull(message = "Bulk Customer Id should not be null")
    @Pattern(regexp = "[1-9]\\d{7}", message = "Bulk customer Id is in wrong format")
    private String bulkCustomerId;
    @NotNull(message = "claimant Reference should not be null")
    private String claimantReference;
    private ClaimantType claimant;
    private DefendantType defendant1;
    private DefendantType defendant2;
    @NotNull @Size(max = 45, message = "particulars value should be less than or equal to 45")
    private String particulars;
    @NotNull(message = "Claim amount should not be null")
    @Min(value = 0, message = "claim amount should not be less than 0")
    @Max(value = 99999, message = "claim amount should not be more than 99999")
    private Long claimAmount;
    private Boolean reserveRightToClaimInterest;
    @NotNull (message = "sotSignature value should not be null")
    private String sotSignature;
    private final String sotSignatureRole = "bulk issuer role";
    private Interest interest;

}
