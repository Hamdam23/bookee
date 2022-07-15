package hamdam.bookee.APIs.image.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileSystemRepository {
    // TODO: 13/07/22 handle npe -> problem removed;)
    // TODO: 13/07/22 custom path -> Status:DONE on 15/07/22
    @Value("${file_upload_path}")
    private String path;

    public String writeFile(byte[] content, String imageName) throws Exception {
        Path newFile = Paths.get(path + imageName);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, content);

        return newFile.toAbsolutePath().toString();
    }

    public FileSystemResource readFile(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception exception) {
            throw new RuntimeException("Image not found");
        }
    }
}
