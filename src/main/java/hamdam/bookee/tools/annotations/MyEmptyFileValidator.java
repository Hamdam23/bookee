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
            assert extension != null;
            return extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg");
        }
        return false;
    }
}
