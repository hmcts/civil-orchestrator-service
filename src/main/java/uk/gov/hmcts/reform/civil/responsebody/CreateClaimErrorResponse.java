package uk.gov.hmcts.reform.civil.responsebody;

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

    private String errorCode;
    private String errorText;

}
