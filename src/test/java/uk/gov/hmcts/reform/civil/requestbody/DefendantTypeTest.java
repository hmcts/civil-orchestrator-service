package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefendantTypeTest {

    private Validator validator;
    private DefendantType defendantType;
    private Set<ConstraintViolation<DefendantType>> constraintViolations;

    @BeforeEach
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    void shouldThrowErrorMessageWhenDefendantNameIsNull() {

        defendantType = DefendantType.builder().address(AddressType.builder().addressLine1("SiteName")
                                                          .postcode("B9111567").build())
            .build();
        constraintViolations = validator.validate(defendantType);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Defendant name should not be null");
    }

    @Test
    void shouldNotThrowErrorMessageWhenDefendantNameIsNotNull() {

        defendantType = DefendantType.builder().name("TestClaimant")
            .address(AddressType.builder().addressLine1("SiteName")
                         .postcode("B9111567").build())
            .build();
        constraintViolations = validator.validate(defendantType);
        assertEquals(constraintViolations.iterator().hasNext(), false);
    }
}
