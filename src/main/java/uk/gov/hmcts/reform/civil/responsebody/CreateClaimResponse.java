package uk.gov.hmcts.reform.civil.responsebody;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CreateClaimResponse {
    @NotNull
    @Pattern(regexp = "[\\dA-Za-z]{8}")
    private String claimNumber;
    private LocalDate issueDate;
    private LocalDate serviceDate;
}
