package uk.gov.hmcts.reform.civil.mappings;

import lombok.*;
import uk.gov.hmcts.reform.civil.modelsdt.ClaimantType;
import uk.gov.hmcts.reform.civil.modelsdt.DefendantType;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimSDT {

    private String claimantReference;
    private ClaimantType claimant;
    private DefendantType defendant1;
    private DefendantType defendant2;
    private String particulars;
    private Integer claimAmount;
    private Boolean reserveRightToClaimInterest;
    private LocalDate claimDate;
    private String sotSignature;

}
