package hamdam.bookee.tools.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MyEmptyFileValidator.class)
public @interface MyValidFile {

    String message() default "{Uploading files other than {jpg, jpeg, png} prohibited!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
