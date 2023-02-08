package xyz.mdpn.song.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AfterDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterDate {
    String message() default "Date should be after 1970/01/01";

    String format() default "yyyy/MM/dd";

    String value() default "1970/01/01";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean including() default true;
}
