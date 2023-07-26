package uk.gov.hmcts.reform.civil.mappings;

import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.utils.MonetaryConversions;

import java.math.BigDecimal;

public class ClaimMappingUtils {

    private ClaimMappingUtils() {
        // private constructor for checkstyle
    }

    public static BigDecimal claimAmount(CreateClaimRequest createClaimRequest) {
        BigDecimal bigDecimal = new BigDecimal(createClaimRequest.getClaimAmount());
        return MonetaryConversions.penniesToPounds(bigDecimal);
    }

}
