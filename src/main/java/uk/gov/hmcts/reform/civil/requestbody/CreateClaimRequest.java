package uk.gov.hmcts.reform.civil.requestbody;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
import uk.gov.hmcts.reform.civil.model.casedata.YesOrNo;


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
@Schema(description = "Schema for Create Claim Request Object")
public class CreateClaimRequest {

    private String sdtRequestId;
    @NotNull(message = "Bulk Customer Id should not be null")
    @Pattern(regexp = "[1-9]\\d{7}", message = "Bulk customer Id is in wrong format")
    @Schema(description = "Bulk customer Id that should be in [1-9]\\d{7} format ", example = "15678908")
    private String bulkCustomerId;
    @NotNull(message = "claimant Reference should not be null")
    @Size(max = 24, message = "claimantReference should be less than or equal to 45")
    @Schema(example = "1568h8992334")
    private String claimantReference;
    @Schema(implementation = ClaimantType.class, description = "Applicant/Claimant details")
    @Valid
    private ClaimantType claimant;
    // default value, bulk requests will either be 1v1 or 1v2.
    private final YesOrNo addApplicant2 = YesOrNo.NO;
    @Schema(implementation = DefendantType.class, description = "Defendant1 details")
    @Valid
    private DefendantType defendant1;
    // default value, bulk request respondents will not be represented on create claim.
    private final YesOrNo specRespondent1Represented = YesOrNo.NO;
    @Schema(implementation = DefendantType.class, description = "Defendent2 details if exists")
    @Valid
    private DefendantType defendant2;
    @NotNull @Size(max = 45, message = "particulars value should be less than or equal to 45")
    @Schema(description = "particulars value should always be less than or equal to 45 characters", example = "particularsValue")
    private String particulars;
    @NotNull(message = "Claim amount should not be null")
    @Min(value = 0, message = "claim amount should not be less than 0")
    @Max(value = 99999, message = "claim amount should not be more than 99999")
    @Schema(description = "claim amount should be between 0 and 99999", example = "87989")
    private Long claimAmount;
    private Boolean reserveRightToClaimInterest;
    @NotNull (message = "sotSignature value should not be null")
    @Schema(example = "signature")
    private String sotSignature;
    @Schema(example = "bulkIssuerRole")
    private final String sotSignatureRole = "bulk issuer role";
    @Schema(implementation = Interest.class)
    @Valid
    private Interest interest;

}
