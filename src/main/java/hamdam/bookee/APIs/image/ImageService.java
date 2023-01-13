package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.helpers.ImageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageEntity uploadImage(MultipartFile file) throws Exception;

    ImageResponseDTO getImageByID(Long id);

    void deleteImageById(Long id);
}
