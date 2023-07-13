package uk.gov.hmcts.reform.civil.requestbody;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interest {

    private Integer interestDailyAmount;
    @NotNull(message = "Interest Owed Date should not be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate interestOwedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate interestClaimDate;
    private Integer claimAmountInterestBase;

}
