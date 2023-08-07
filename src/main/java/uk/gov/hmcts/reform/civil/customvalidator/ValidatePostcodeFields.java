package uk.gov.hmcts.reform.civil.customvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldMatchPostcodeValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePostcodeFields {

    String message() default "";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    String field() default "";

    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD })
    @interface List {
        ValidatePostcodeFields[] value();
    }

}
