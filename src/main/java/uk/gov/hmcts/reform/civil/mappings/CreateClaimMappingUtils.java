package uk.gov.hmcts.reform.civil.mappings;

import uk.gov.hmcts.reform.civil.model.casedata.CorrectEmail;
import uk.gov.hmcts.reform.civil.model.casedata.Party;
import uk.gov.hmcts.reform.civil.model.casedata.YesOrNo;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.utils.MonetaryConversions;

import java.math.BigDecimal;

public class CreateClaimMappingUtils {

    private CreateClaimMappingUtils() {
        // private constructor for checkstyle
    }

    public static BigDecimal claimAmount(CreateClaimRequest createClaimRequest) {
        BigDecimal bigDecimal = new BigDecimal(createClaimRequest.getClaimAmount());
        return MonetaryConversions.penniesToPounds(bigDecimal);
    }

    public static Party.Type setClaimantType() {
        return Party.Type.ORGANISATION;
    }

    public static Party.Type setDefendantType() {
        return Party.Type.INDIVIDUAL;
    }

    public static YesOrNo checkRespondent2(CreateClaimRequest createClaimRequest) {
        if (createClaimRequest.getDefendant2() != null) {
            return YesOrNo.YES;
        } else {
            return YesOrNo.NO;
        }
    }

    public static YesOrNo claimInterest(CreateClaimRequest createClaimRequest) {
        if (createClaimRequest.getReserveRightToClaimInterest().equals(true)) {
            return YesOrNo.YES;
        } else {
            return YesOrNo.NO;
        }
    }

    public static CorrectEmail checkCorrectEmail() {
        CorrectEmail correctEmail = new CorrectEmail();
        correctEmail.setCorrect(YesOrNo.NO);
        return correctEmail;
    }

}
