package uk.gov.hmcts.reform.civil.responsebody;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateClaimErrorResponseTest {

    private Validator validator;
    private CreateClaimErrorResponse claimErrorResponse;
    private Set<ConstraintViolation<CreateClaimErrorResponse>> constraintViolations;

    @BeforeEach
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    void shouldThrowErrorMessageWhenClaimNumberIsNotInValidFormat() {
        claimErrorResponse = CreateClaimErrorResponse.builder().claimNumber("abc123678")
            .errorCode("401")
            .errorText("Unknown user")
            .build();
        constraintViolations = validator.validate(claimErrorResponse);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Claim Reference number is in Incorrect format");
    }

    @Test
    void shouldThrowErrorMessageWhenErrorCodeIsNull() {
        claimErrorResponse = CreateClaimErrorResponse.builder().claimNumber("123678789")
            .errorText("Unknown user")
            .build();
        constraintViolations = validator.validate(claimErrorResponse);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Error code should not be null");
    }

    @Test
    void shouldThrowErrorMessageWhenErrorTextIsNull() {
        claimErrorResponse = CreateClaimErrorResponse.builder().claimNumber("123678789")
            .errorCode("401")
            .build();
        constraintViolations = validator.validate(claimErrorResponse);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Error text should not be null");
    }

}
