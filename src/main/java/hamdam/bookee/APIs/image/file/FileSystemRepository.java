package hamdam.bookee.APIs.image.file;

import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileSystemRepository {

    public String writeFilePath(byte[] content, Path path) throws IOException {
        Files.write(path, content);
        return path.toAbsolutePath().toString();
    }

    public FileSystemResource readFileFromPath(Path path) {
        try {
            return new FileSystemResource(path);
            //TODO test catch part
        } catch (IllegalArgumentException exception) {
            throw new ResourceNotFoundException("location", "Image", path.toAbsolutePath().toString());
        }
    }
}
