package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.helpers.ImageDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageEntity uploadImage(MultipartFile file) throws Exception;

    ImageDTO getImageByID(Long id);

    void deleteImageById(Long id);
}
