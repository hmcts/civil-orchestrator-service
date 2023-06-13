package uk.gov.hmcts.reform.civil.modelsdt;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClaimantType {

    private String name;
    private AddressType address;

}
