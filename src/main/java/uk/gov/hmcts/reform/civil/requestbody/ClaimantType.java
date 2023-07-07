package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.constraints.NotNull;
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
public class ClaimantType {

    @NotNull(message = "Claimant name should not be null")
    private String name;
    private AddressType address;

}
