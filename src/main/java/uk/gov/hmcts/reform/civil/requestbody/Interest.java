package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interest {

    private Integer interestDailyAmount;
    @NotNull(message = "Interest Owed Date should not be null")
    private LocalDate interestOwedDate;
    private LocalDate interestClaimDate;
    private Integer claimAmountInterestBase;

}
