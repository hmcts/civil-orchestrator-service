package uk.gov.hmcts.reform.civil.customvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateFields {

    String message() default "Fields cannot be identical..!";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    String field() default "";
    String parentField() default "";
    String fieldMatch() default "";

    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD })
    @interface List {
        ValidateFields[] value();
    }

}
