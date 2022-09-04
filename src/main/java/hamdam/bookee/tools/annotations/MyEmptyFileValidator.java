package hamdam.bookee.tools.annotations;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyEmptyFileValidator implements ConstraintValidator<MyValidFile, MultipartFile> {

    @Override
    public void initialize(MyValidFile constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value != null && !value.isEmpty()) {
            String extension = FilenameUtils.getExtension(value.getOriginalFilename());
            // TODO: 9/2/22 why there is assertion error when extension is null = DONE(04.09.22)
            // TODO: don't use ALT+ENTER to fix it!
            if (extension != null) {
                return extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg");
            }
        }
        return false;
    }
}
