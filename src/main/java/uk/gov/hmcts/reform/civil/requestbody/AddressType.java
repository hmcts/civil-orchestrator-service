package uk.gov.hmcts.reform.civil.requestbody;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class AddressType {
    @Size(max = 150, message = "AddressLine1 value should be less than or equal to 150 characters")
    @Schema(example = "Flat 2, Caver-sham House 15-17")
    private String addressLine1;
    @Size(max = 50, message = "AddressLine2 value should be less than or equal to 50 characters")
    @Schema(example = "Church Road, Kent")
    private String addressLine2;
    @Size(max = 50, message = "AddressLine3 value should be less than or equal to 50 characters")
    @Schema(example = "Kent")
    private String addressLine3;
    @Size(max = 50, message = "AddressLine4 value should be less than or equal to 50 characters")
    @Schema(example = "UNITED KINGDOM")
    private String addressLine4;
    @NotNull(message = "Postcode value should not be null")
    @Size(max = 14, message = "PostCode value should be less than or equal to 14 characters")
    @Schema(example = "RG4 7AA")
    private String postcode;
}
