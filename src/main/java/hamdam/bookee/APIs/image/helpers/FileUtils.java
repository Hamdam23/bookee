package hamdam.bookee.APIs.image.helpers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class FileUtils {

    /**
     * It takes a file name, removes the extension,
     * replaces spaces with dashes,
     * and appends the current time in milliseconds to the end of the file name
     *
     * @param file The file that you want to get the unique name for.
     * @return A unique file name.
     */
    public static String getUniqueFileName(MultipartFile file) {
        String fileNameWithoutExt = FilenameUtils.removeExtension(file.getOriginalFilename());
        if (fileNameWithoutExt == null || fileNameWithoutExt.isBlank()) {
            fileNameWithoutExt = "image";
        } else {
            fileNameWithoutExt = fileNameWithoutExt.replace(" ", "-");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // Taking the file name, removing the extension, replacing spaces with dashes,
        // and appending the current time in milliseconds to the end of the file name.
        return fileNameWithoutExt + "-" + new Date().getTime() + "." + extension;
    }
}
