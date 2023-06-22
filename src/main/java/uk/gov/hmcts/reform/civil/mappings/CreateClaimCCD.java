package uk.gov.hmcts.reform.civil.mappings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.reform.civil.model.MockOrgPolicy;
import uk.gov.hmcts.reform.civilcommonsmock.civil.enums.YesOrNo;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.CorrectEmail;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.Fee;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.IdamUserDetails;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.Party;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.SolicitorReferences;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.StatementOfTruth;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateClaimCCD {

    private SolicitorReferences solicitorReferences;
    private Party applicant1;
    // TODO: CorrectEmail has a YesNoValue field, which is causing issues
    private CorrectEmail applicantSolicitor1CheckEmail;
    // TODO: civil service has two organsationpolicy classes, one from civil-commons, so mock that one.
    private MockOrgPolicy applicant1OrganisationPolicy;
    private Party respondent1;
    // TODO: Random issue with mapStruct, not accepting actual value of "addRespondent2"
    private YesOrNo adddRespondent2;
    private Party respondent2;
    private String detailsOfClaim;
    private BigDecimal totalClaimAmount;
    // TODO: YesNoValue field, which is causing issues
    private YesOrNo claimInterest;
    private LocalDate interestFromSpecificDate;
    private String interestFromSpecificDateDescription;
    private StatementOfTruth uiStatementOfTruth;
    private IdamUserDetails applicantSolicitor1UserDetails;
    // TODO: Fee has a MoneyGBP field, causing issues e.g. "1999 is not valid MoneyGBP"
    private Fee claimFee;

}
