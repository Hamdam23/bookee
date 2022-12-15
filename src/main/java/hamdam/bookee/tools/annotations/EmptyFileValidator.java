package hamdam.bookee.tools.annotations;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmptyFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private final Set<ValidFile.Extension> extensions = new HashSet<>();

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        extensions.addAll(List.of(constraintAnnotation.extensions()));
        for (ValidFile.ExtensionGroup extensionGroup : constraintAnnotation.extensionGroups()) {
            extensions.addAll(List.of(extensionGroup.getExtensions()));
        }
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value != null && !value.isEmpty()) {
            String extension = FilenameUtils.getExtension(value.getOriginalFilename());
            return extension != null && extensions.stream()
                    .map(enumValue -> enumValue.name().toLowerCase())
                    .anyMatch(validExtension -> validExtension.equals(extension));
        }
        return false;
    }
}
