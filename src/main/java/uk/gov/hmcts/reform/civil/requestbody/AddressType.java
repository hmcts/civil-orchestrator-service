package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressType {
    @Max(150)
    private String addressLine1;
    @Max(50)
    private String addressLine2;
    @Max(50)
    private String addressLine3;
    @Max(50)
    private String addressLine4;
    @Max(14)
    private String postcode;
}
