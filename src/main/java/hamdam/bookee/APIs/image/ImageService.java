package hamdam.bookee.APIs.image;

import hamdam.bookee.tools.exceptions.ApiResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageEntity uploadImage(MultipartFile file) throws Exception;

    FileSystemResource downloadImage(String location);

    ImageDTO getImageByID(long id);

    // TODO: 9/2/22 why it returns ImageDTO, but byId returns Image itself?
    Page<ImageDTO> getAllImages(Pageable pageable);

    ApiResponse deleteImageById(long id);
}
