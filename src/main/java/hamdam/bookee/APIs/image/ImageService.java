package hamdam.bookee.APIs.image;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image uploadImage(MultipartFile file) throws Exception;

    FileSystemResource downloadImage(String location);

    Image getImageByID(long id);

    List<ImageDTO> getAllImages();

    void deleteImageById(long id);
}
