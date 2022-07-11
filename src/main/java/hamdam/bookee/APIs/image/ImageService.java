package hamdam.bookee.APIs.image;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image getImageByID(long id);
    Image uploadImage(MultipartFile file) throws Exception;
    FileSystemResource downloadImage(String location);
}
