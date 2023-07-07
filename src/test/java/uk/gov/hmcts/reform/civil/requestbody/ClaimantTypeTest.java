package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClaimantTypeTest {
    private Validator validator;
    private ClaimantType claimantType;
    private Set<ConstraintViolation<ClaimantType>> constraintViolations;

    @BeforeEach
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    void shouldThrowErrorMessageWhenClaimantNameIsNull() {

        claimantType = ClaimantType.builder().address(AddressType.builder().addressLine1("SiteName")
                                                          .postcode("B9111567").build())
            .build();
        constraintViolations = validator.validate(claimantType);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Claimant name should not be null");
    }

    @Test
    void shouldNotThrowErrorMessageWhenClaimantNameIsNotNull() {

        claimantType = ClaimantType.builder().name("TestClaimant")
        .address(AddressType.builder().addressLine1("SiteName")
                                                          .postcode("B9111567").build())
            .build();
        constraintViolations = validator.validate(claimantType);
        assertEquals(constraintViolations.iterator().hasNext(), false);
    }
}
