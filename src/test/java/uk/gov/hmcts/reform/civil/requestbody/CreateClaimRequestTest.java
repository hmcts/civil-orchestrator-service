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
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    void shouldThrowErrorMessageWhenBulkClaimIdIsNotInValidFormat() {
        claimRequest = CreateClaimRequest.builder().bulkCustomerId("abc123678")
             .claimAmount(Long.valueOf(7890))
             .claimantReference("1568h8992334")
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
             .particulars("particulars")
             .sotSignature("sotSignatureExample")
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Bulk customer Id is in wrong format");
    }

    @Test
    void shouldThrowErrorMessageWhenBulkClaimIdIsNull() {
        claimRequest = CreateClaimRequest.builder()
            .claimAmount(Long.valueOf(7890))
            .claimantReference("1568h8992334")
            .particulars("particulars")
            .sotSignature("sotSignatureExample")
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Bulk Customer Id should not be null");
    }

    @Test
    void shouldThrowErrorMessageWhenClaimantReferenceIsNull() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(7890))
            .particulars("particulars")
            .sotSignature("sotSignatureExample")
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "claimant Reference should not be null");
    }

    @Test
    void shouldThrowErrorMessageWhenParticularsExceedsMaximumLength() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(8000))
            .particulars("particulars length should ne less than or equal to forty five. testing is in progress")
            .claimantReference("1568h8992334")
            .sotSignature("sotSignatureExample")
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "particulars value should be less than or equal to 45");
    }

    @Test
    void shouldThrowErrorMessageWhenSotSignatureIsNull() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(8000))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "sotSignature value should not be null");
    }

    @Test
    void shouldThrowErrorMessageWhenClaimAmountIsNull() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .sotSignature("sotSignatureExample")
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Claim amount should not be null");
    }

    @Test
    void shouldThrowErrorMessageWhenClaimAmountIsLessThanZero() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(-1))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .sotSignature("sotSignatureExample")
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "claim amount should not be less than 0");
    }

    @Test
    void shouldThrowErrorMessageWhenClaimAmountIsGreaterThanMaximumValue() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(999999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .sotSignature("sotSignatureExample")
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(), "claim amount should not be more than 99999");
    }

    @Test
    void shouldThrowCustomErrorWhenDefendant1NameIsEqualToDefendant2Name() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").build())
            .defendant1(DefendantType.builder().name("defendant").build())
            .defendant2(DefendantType.builder().name("defendant").build())
            .sotSignature("sotSignatureExample")
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.iterator().next().getMessage(),
                     "Second defendant cannot have an identical name to the first defendant");
    }

    @Test
    void shouldNotThrowErrorWhenDefendant1NameIsNotEqualToDefendant2Name() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .sotSignature("sotSignatureExample")
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.isEmpty(), true);
    }

    @Test
    void shouldProceedWithoutErrorsWhenDefendant2DoesNotExist() {

        claimRequest = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .sotSignature("sotSignatureExample")
            .build();
        constraintViolations = validator.validate(claimRequest);
        assertEquals(constraintViolations.isEmpty(), true);
    }
}
