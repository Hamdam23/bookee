package hamdam.bookee.tools.annotations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static hamdam.bookee.tools.annotations.ValidFile.Extension.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EmptyFileValidator.class)
public @interface ValidFile {

    Extension[] extensions() default {};

    ExtensionGroup[] extensionGroups() default {};

    String message() default "Uploaded not allowed file type!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum Extension {
        PNG,
        JPEG,
        JPG,
        PDF,
        DOC,
        DOCX,
        EXE,
        ZIP,
        RAR
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    enum ExtensionGroup {
        IMAGES(new Extension[]{PNG, JPEG, JPG}),
        DOCUMENTS(new Extension[]{DOC, DOCX, PDF});

        private Extension[] extensions;
    }
}
