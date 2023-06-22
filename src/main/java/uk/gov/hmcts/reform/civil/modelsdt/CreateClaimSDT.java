package uk.gov.hmcts.reform.civil.modelsdt;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
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
    private String sotSignatureRole = "bulk issuer role";
    private Interest interest;

}
