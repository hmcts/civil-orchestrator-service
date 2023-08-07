package uk.gov.hmcts.reform.civil.customvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import uk.gov.hmcts.reform.civil.validation.PostcodeValidator;

public class FieldMatchValidator implements ConstraintValidator<ValidateFields, Object> {

    private String field1Value;
    private String parentField;
    private String field2Value;

    @Override
    public void initialize(ValidateFields constraintAnnotation) {
        if (constraintAnnotation.field() != null && constraintAnnotation.fieldMatch() != null) {
            this.field1Value = constraintAnnotation.field();
            this.field2Value = constraintAnnotation.fieldMatch();
            this.parentField = constraintAnnotation.parentField();
        }
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        Object fieldValue = new BeanWrapperImpl(value)
            .getPropertyValue(field1Value);
        Object parentFieldValue = new BeanWrapperImpl(value)
            .getPropertyValue(parentField);
        Object fieldMatchValue = null;
        if (parentFieldValue != null) {
            fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(field2Value);
        }

        if (value == null) {
            return true;
        }
        if (fieldValue != null && fieldMatchValue != null) {
            return !fieldValue.equals(fieldMatchValue);
        }

        return true;
    }
}
