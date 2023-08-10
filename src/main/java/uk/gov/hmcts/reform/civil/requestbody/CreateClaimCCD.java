package uk.gov.hmcts.reform.civil.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.reform.civil.model.casedata.CorrectEmail;
import uk.gov.hmcts.reform.civil.model.casedata.IdamUserDetails;
import uk.gov.hmcts.reform.civil.model.casedata.OrganisationPolicy;
import uk.gov.hmcts.reform.civil.model.casedata.Party;
import uk.gov.hmcts.reform.civil.model.casedata.SolicitorReferences;
import uk.gov.hmcts.reform.civil.model.casedata.StatementOfTruth;
import uk.gov.hmcts.reform.civil.model.casedata.YesOrNo;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateClaimCCD {

    private String sdtRequestIdFromSdt;
    private SolicitorReferences solicitorReferences;
    private Party applicant1;
    // TODO: Random issue with mapStruct, not accepting actual value of "addRespondent2"
    @JsonProperty("addApplicant2")
    private YesOrNo adddApplicant2;
    private CorrectEmail applicantSolicitor1CheckEmail;
    private OrganisationPolicy applicant1OrganisationPolicy;
    private Party respondent1;
    private YesOrNo specRespondent1Represented;
    @JsonProperty("addRespondent2")
    private YesOrNo adddRespondent2;
    private Party respondent2;
    private String detailsOfClaim;
    //private List<ClaimAmountBreakup> claimAmountBreakup;
    private BigDecimal totalClaimAmount;
    private YesOrNo claimInterest;
    private LocalDate interestFromSpecificDate;
    private String interestFromSpecificDateDescription;
    private StatementOfTruth uiStatementOfTruth;
    private IdamUserDetails applicantSolicitor1UserDetails;
    // TODO: Add in fields if interest was requested
    // private InterestClaimOptions interestClaimOptions;
    // private SameRateInterestSelection sameRateInterestSelection;
    // etc

}
