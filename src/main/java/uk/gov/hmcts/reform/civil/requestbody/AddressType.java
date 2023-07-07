package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.constraints.Size;
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
    @Size(max = 150, message = "AddressLine1 value should be less than or equal to 150 characters")
    private String addressLine1;
    @Size(max = 50, message = "AddressLine2 value should be less than or equal to 50 characters")
    private String addressLine2;
    @Size(max = 50, message = "AddressLine3 value should be less than or equal to 50 characters")
    private String addressLine3;
    @Size(max = 50, message = "AddressLine4 value should be less than or equal to 50 characters")
    private String addressLine4;
    @Size(max = 14, message = "PostCode value should be less than or equal to 14 characters")
    private String postcode;
}
