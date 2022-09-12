package hamdam.bookee.tools.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EmptyFileValidator.class)
public @interface ValidFile {

    String message() default "{Uploading files other than {jpg, jpeg, png} prohibited!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
