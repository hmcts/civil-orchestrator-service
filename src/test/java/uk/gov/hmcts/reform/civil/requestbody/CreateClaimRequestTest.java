package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateClaimRequestTest {

    private Validator validator;
    private CreateClaimRequest claimRequest;
    private Set<ConstraintViolation<CreateClaimRequest>> constraintViolations;

    @BeforeEach
    void init(){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    void shouldThrowErrorMessageWhenBulkClaimIdIsNotInValidFormat() {

         claimRequest = CreateClaimRequest.builder().bulkCustomerId("abc123678")
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Bulk customer Id is in wrong format");
    }
}
