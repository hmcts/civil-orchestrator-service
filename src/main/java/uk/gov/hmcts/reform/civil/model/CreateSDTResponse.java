package uk.gov.hmcts.reform.civil.model;

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
public class CreateSDTResponse {

    private String claimNumber;
    private String errorCode;
    private String errorText;

}
