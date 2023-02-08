package xyz.mpdn.resource.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MimeTypeValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MimeType {
    String message() default "Invalid file type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value() default "application/octet-stream";
}
