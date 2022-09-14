package hamdam.bookee.tools.annotations;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmptyFileValidator {
// implements ConstraintValidator<ValidFile, MultipartFile>
//    @Override
//    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
//        if (value != null && !value.isEmpty()) {
//            String extension = FilenameUtils.getExtension(value.getOriginalFilename());
//            return extension != null &&
//                    (extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg"));
//
//        }
//        return false;
//    }
}
