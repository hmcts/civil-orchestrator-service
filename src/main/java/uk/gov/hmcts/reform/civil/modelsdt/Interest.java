package uk.gov.hmcts.reform.civil.modelsdt;

import lombok.*;

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
