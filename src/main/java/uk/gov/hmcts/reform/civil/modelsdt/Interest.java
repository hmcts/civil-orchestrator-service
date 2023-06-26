package uk.gov.hmcts.reform.civil.modelsdt;

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
@ToString
public class Interest {

    private String dailyAmount;
    private LocalDate owedDate;
    private LocalDate claimDate;
    private String claimAmountInterestBase;

}
