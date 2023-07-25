package uk.gov.hmcts.reform.civil.requestbody;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimantType {

    @NotNull(message = "Claimant name should not be null")
    @Schema(example = "Dr Jane Doe")
    private String name;
    @Valid
    @Schema(implementation = AddressType.class)
    private AddressType address;

}
