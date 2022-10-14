package hamdam.bookee.APIs.image.file;

import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileSystemRepository {

//    @Value("${file_upload_path}")
//    private String path;

    public String writeFileToPath(byte[] content, Path path) throws IOException {
//        Path newFile = Paths.get(path + imageName);
//        Files.createDirectories(path.getParent());
        Files.write(path, content);

        return path.toAbsolutePath().toString();
    }

//    public String writeFile(byte[] content, String imageName) throws Exception {
//        Path newFile = Paths.get(path + imageName);
//        Files.createDirectories(newFile.getParent());
//
//        Files.write(newFile, content);
//
//        return newFile.toAbsolutePath().toString();
//    }

    public FileSystemResource readFileFromPath(Path path) {
        try {
            return new FileSystemResource(path);
        } catch (Exception exception) {
            throw new ResourceNotFoundException("location", "Image", path.toAbsolutePath().toString());
        }
    }

//    public FileSystemResource readFile(String location) {
//        try {
//            return new FileSystemResource(Paths.get(location));
//        } catch (Exception exception) {
//            throw new ResourceNotFoundException("location", "Image", location);
//        }
//    }
}
