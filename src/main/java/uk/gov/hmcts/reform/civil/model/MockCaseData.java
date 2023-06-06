package uk.gov.hmcts.reform.civil.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import uk.gov.hmcts.reform.civil.enums.SpecClaimTimelineList;
import uk.gov.hmcts.reform.civil.enums.YesOrNo;
import uk.gov.hmcts.reform.civil.model.interestcalc.InterestClaimFromType;
import uk.gov.hmcts.reform.civil.model.interestcalc.InterestClaimOptions;
import uk.gov.hmcts.reform.civil.model.interestcalc.InterestClaimUntilType;
import uk.gov.hmcts.reform.civil.model.interestcalc.SameRateInterestSelection;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class MockCaseData {

    private SolicitorReferences solicitorReferences;
    private DummyParty applicant1;
    private YesOrNo addApplicant2;
    private CorrectEmail applicantSolicitor1CheckEmail;
    private OrganisationPolicy applicant1OrganisationPolicy;
    private YesOrNo specApplicantCorrespondenceAddressRequired;
    private Address specApplicantCorrespondenceAddressdetails;
    private DummyParty Respondent1;
    private YesOrNo specRespondent1RepresentedLabel;
    private YesOrNo addRespondent2;
    private DummyParty Respondent2;
    private YesOrNo specRespondent2Represented;
    private String detailsOfClaim;
    //private Document specClaimDetailsDocumentFiles;
    private SpecClaimTimelineList specClaimTimelineList;
    private List<TimelineOfEvents> timelineOfEvents;
    private List<Evidence> speclistYourEvidenceList;
    private List<ClaimAmountBreakup> claimAmountBreakup;
    private YesOrNo claimInterest;
    private InterestClaimOptions interestClaimOptions;
    private SameRateInterestSelection sameRateInterestSelection;
    private InterestClaimFromType interestClaimFrom;
    private LocalDate interestFromSpecificDate;
    private String interestFromSpecificDateDescription;
    private InterestClaimUntilType interestClaimUntil;
    private Fee claimFee;
    private StatementOfTruth uiStatementOfTruth;
}
