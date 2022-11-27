package hamdam.bookee.APIs.image.helpers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class FileUtils {

    public static String getUniqueFileName(MultipartFile file) {
        String fileNameWithoutExt = FilenameUtils.removeExtension(file.getOriginalFilename());
        if (fileNameWithoutExt == null || fileNameWithoutExt.isBlank()) {
            fileNameWithoutExt = "image";
        } else {
            fileNameWithoutExt = fileNameWithoutExt.replace(" ", "-");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return fileNameWithoutExt + "-" + new Date().getTime() + "." + extension;
    }
}
