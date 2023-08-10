package uk.gov.hmcts.reform.civil.mappings;

import uk.gov.hmcts.reform.civil.model.casedata.*;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.utils.MonetaryConversions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateClaimMappingUtils {

    private CreateClaimMappingUtils() {
        // private constructor for checkstyle
    }

    public static List<ClaimAmountBreakup> claimAmountDetails(CreateClaimRequest createClaimRequest) {
        List<ClaimAmountBreakup> claimAmountDetails = new ArrayList<>();
        BigDecimal bigDecimal = new BigDecimal(createClaimRequest.getClaimAmount());
        ClaimAmountBreakup claimAmountBreakup = ClaimAmountBreakup.builder().value(ClaimAmountBreakupDetails
                                               .builder()
                                               .claimAmount(MonetaryConversions.penniesToPounds(bigDecimal))
                                               .claimReason("place holder reason")
                                               .build()).build();
        claimAmountDetails.add(claimAmountBreakup);
        return claimAmountDetails;
    }

    public static BigDecimal totalClaimAmount(CreateClaimRequest createClaimRequest) {
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
