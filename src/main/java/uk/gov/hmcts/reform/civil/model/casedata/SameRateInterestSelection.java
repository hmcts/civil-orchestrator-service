package uk.gov.hmcts.reform.civil.model.casedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class SameRateInterestSelection {

    //private final SameRateInterestType sameRateInterestType;
    private final BigDecimal differentRate;
    private final String differentRateReason;

}
