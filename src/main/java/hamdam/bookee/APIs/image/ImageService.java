package hamdam.bookee.APIs.image;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image uploadImage(MultipartFile file) throws Exception;

    FileSystemResource downloadImage(String location);

    Image getImageByID(long id);

    // TODO: 9/2/22 why it returns ImageDTO, but byId returns Image itself?
    List<ImageDTO> getAllImages();

    void deleteImageById(long id);
}
