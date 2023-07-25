package uk.gov.hmcts.reform.civil.requestbody;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "7")
    private Integer interestDailyAmount;
    @NotNull(message = "Interest Owed Date should not be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2023-07-25")
    private LocalDate interestOwedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2023-07-25")
    private LocalDate interestClaimDate;
    @Schema(example = "6")
    private Integer claimAmountInterestBase;

}
