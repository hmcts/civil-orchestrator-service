package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InterestTest {

    private Validator validator;
    private Interest interest;
    private Set<ConstraintViolation<Interest>> constraintViolations;

    @BeforeEach
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    void shouldThrowErrorMessageWhenInterestOwedDateIsNull() {

        interest = Interest.builder().interestDailyAmount(200)
            .interestClaimDate(LocalDate.now())
            .claimAmountInterestBase(20)
            .build();
        constraintViolations = validator.validate(interest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Interest Owed Date should not be null");
    }

    @Test
    void shouldNotThrowErrorMessageWhenInterestOwedDateIsNotNull() {

        interest = Interest.builder().interestDailyAmount(200)
            .interestOwedDate(LocalDate.now())
            .interestClaimDate(LocalDate.now())
            .claimAmountInterestBase(20)
            .build();
        constraintViolations = validator.validate(interest);
        assertEquals(constraintViolations.isEmpty(), true);
    }
}
