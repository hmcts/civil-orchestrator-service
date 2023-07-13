package uk.gov.hmcts.reform.civil.requestbody;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressTypeTest {

    private Validator validator;
    private AddressType addressType;
    private Set<ConstraintViolation<AddressType>> constraintViolations;

    @BeforeEach
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    void shouldThrowErrorMessageWhenAddressLineExceedsMaximumLength() {

        addressType = AddressType.builder().addressLine1("12340000 Site Name000000 - Address9999999 - 28000 UnitedKingdom"
                                                             + "testing addressline length whether it is more than 150 characters or not"
                                                             + "It should not exceed more than 150 characters to proceed further to create"
                                                             + " a claim from SDT")
            .addressLine2("SiteName Street")
            .addressLine3("SiteName Road")
            .addressLine4("UNITED KINGDOM")
            .postcode("POSTCODE")
            .build();
        constraintViolations = validator.validate(addressType);
        assertEquals(constraintViolations.iterator().next().getMessage(), "AddressLine1 value should be less than or equal to 150 characters");
    }

    @Test
    void shouldThrowErrorMessageWhenAddressLine2ExceedsMaximumLength() {

        addressType = AddressType.builder().addressLine1("Site Name000000 - Address9999999 - 28000 UnitedKingdom")
            .addressLine2("SiteName Street - Site Name000000 - Address9999999 - 28000 UnitedKingdom")
            .addressLine3("SiteName Road")
            .addressLine4("UNITED KINGDOM")
            .postcode("POSTCODE")
            .build();
        constraintViolations = validator.validate(addressType);
        assertEquals(constraintViolations.iterator().next().getMessage(), "AddressLine2 value should be less than or equal to 50 characters");
    }

    @Test
    void shouldThrowErrorMessageWhenAddressLine3ExceedsMaximumLength() {

        addressType = AddressType.builder().addressLine1("Site Name000000 - Address9999999 - 28000 UnitedKingdom")
            .addressLine2("SiteName Street ")
            .addressLine3("SiteName Road - - Site Name000000 - Address9999999 - 28000 UnitedKingdom")
            .addressLine4("UNITED KINGDOM")
            .postcode("POSTCODE")
            .build();
        constraintViolations = validator.validate(addressType);
        assertEquals(constraintViolations.iterator().next().getMessage(), "AddressLine3 value should be less than or equal to 50 characters");
    }

    @Test
    void shouldThrowErrorMessageWhenAddressLine4ExceedsMaximumLength() {

        addressType = AddressType.builder().addressLine1("Site Name000000 - Address9999999 - 28000 UnitedKingdom")
            .addressLine2("SiteName Street ")
            .addressLine3("SiteName Road ")
            .addressLine4("Site Name000000 - Address9999999 - 28000 UnitedKingdom")
            .postcode("POSTCODE")
            .build();
        constraintViolations = validator.validate(addressType);
        assertEquals(constraintViolations.iterator().next().getMessage(), "AddressLine4 value should be less than or equal to 50 characters");
    }

    @Test
    void shouldThrowErrorMessageWhenPostCodeExceedsMaximumLength() {

        addressType = AddressType.builder().addressLine1("Site Name000000 - Address9999999 - 28000 UnitedKingdom")
            .addressLine2("SiteName Street ")
            .addressLine3("SiteName Road ")
            .addressLine4("Site Name000000")
            .postcode("POSTCODE- B9945673 3GT")
            .build();
        constraintViolations = validator.validate(addressType);
        assertEquals(constraintViolations.iterator().next().getMessage(), "PostCode value should be less than or equal to 14 characters");
    }

    @Test
    void shouldThrowErrorMessageWhenPostCodeIsNull() {

        addressType = AddressType.builder().addressLine1("Site Name000000 - Address9999999 - 28000 UnitedKingdom")
            .addressLine2("SiteName Street ")
            .addressLine3("SiteName Road ")
            .addressLine4("Site Name000000")
            .build();
        constraintViolations = validator.validate(addressType);
        assertEquals(constraintViolations.iterator().next().getMessage(), "Postcode value should not be null");
    }
}
