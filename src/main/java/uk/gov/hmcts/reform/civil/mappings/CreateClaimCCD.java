package uk.gov.hmcts.reform.civil.mappings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.reform.civil.enums.YesOrNo;
import uk.gov.hmcts.reform.civil.model.ClaimAmountBreakup;
import uk.gov.hmcts.reform.civil.model.CorrectEmail;
import uk.gov.hmcts.reform.civil.model.DummyParty;
import uk.gov.hmcts.reform.civil.model.OrganisationPolicy;
import uk.gov.hmcts.reform.civil.model.SolicitorReferences;
import uk.gov.hmcts.reform.civil.model.StatementOfTruth;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateClaimCCD {

    private SolicitorReferences solicitorReferences;
    private DummyParty applicant1;
    private CorrectEmail applicantSolicitor1CheckEmail;
    private OrganisationPolicy applicant1OrganisationPolicy;
    private DummyParty respondent1;
    private YesOrNo AddRespondent2;
    private DummyParty respondent2;
    private String detailsOfClaim;
    private List<ClaimAmountBreakup> claimAmountBreakup;
    private YesOrNo claimInterest;
    private LocalDate interestFromSpecificDate;
    private StatementOfTruth uiStatementOfTruth;

}
