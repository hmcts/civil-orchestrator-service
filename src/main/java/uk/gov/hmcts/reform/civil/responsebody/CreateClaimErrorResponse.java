package uk.gov.hmcts.reform.civil.responsebody;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CreateClaimErrorResponse implements CreateClaimResponse {

    @NotNull @Pattern(regexp = "[\\dA-Za-z]{8}", message = "Claim Reference number is in Incorrect format")
    private String claimNumber;
    @NotNull(message = "Error code should not be null")
    private String errorCode;
    @NotNull(message = "Error text should not be null")
    private String errorText;

}
