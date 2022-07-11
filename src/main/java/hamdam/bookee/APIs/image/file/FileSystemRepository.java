package hamdam.bookee.APIs.image.file;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileSystemRepository {
    String RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath();

    public String writeFile(byte[] content, String imageName) throws Exception {
        Path newFile = Paths.get(RESOURCES_DIR + imageName);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, content);

        return newFile.toAbsolutePath().toString();
    }

    public FileSystemResource readFile(String location) {
        try{
            return new FileSystemResource(Paths.get(location));
        } catch (Exception exception){
            throw new RuntimeException("Image not found");
        }
    }
}
