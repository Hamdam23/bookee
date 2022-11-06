package hamdam.bookee.APIs.image.file;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileSystemRepository {

    public String writeFileToPath(byte[] content, Path path) throws IOException {
        Files.write(path, content);
        return path.toAbsolutePath().toString();
    }

    public FileSystemResource readFileFromPath(Path path) {
        return new FileSystemResource(path);
    }
}
