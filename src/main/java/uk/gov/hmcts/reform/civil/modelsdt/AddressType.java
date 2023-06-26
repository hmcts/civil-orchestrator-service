package uk.gov.hmcts.reform.civil.modelsdt;

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

    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private String postcode;
}
