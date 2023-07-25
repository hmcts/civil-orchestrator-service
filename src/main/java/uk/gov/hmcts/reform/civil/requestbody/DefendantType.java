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
@Schema(description = "Defendant details")
public class DefendantType {

    @NotNull(message = "Defendant name should not be null")
    @Schema(example = "Sir John Doe")
    private String name;
    @Schema(implementation = AddressType.class)
    @Valid
    private AddressType address;

}
