package uk.gov.hmcts.reform.civil.customvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.validation.PostcodeValidator;

public class FieldMatchPostcodeValidator implements ConstraintValidator<ValidatePostcodeFields, Object> {

    private String field1Value;
    private PostcodeValidator postcodeValidator;
    public void setPostcodeValidator(PostcodeValidator postcodeValidator) {
        this.postcodeValidator = postcodeValidator;
    }

    @Override
    public void initialize(ValidatePostcodeFields constraintAnnotation) {
        if (constraintAnnotation.field() != null) {
            this.field1Value = constraintAnnotation.field();
        }
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        if (value != null) {
            Object fieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(field1Value);
            if (fieldValue == null){
                return true;
            }
            if (!postcodeValidator.validate(fieldValue.toString()).isEmpty()){
                return false;
            }
        }
        return true;
    }

}
