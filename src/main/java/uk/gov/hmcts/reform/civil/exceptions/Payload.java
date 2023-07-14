package uk.gov.hmcts.reform.civil.exceptions;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class Payload {
    @NotBlank
    private String name;
    @Min(value = 18)
    private int age;
}
