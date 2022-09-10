package hamdam.bookee.APIs.image;

import hamdam.bookee.tools.exeptions.ResponseSettings;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ImagEntity uploadImage(MultipartFile file) throws Exception;

    FileSystemResource downloadImage(String location);

    ImageDTO getImageByID(long id);

    // TODO: 9/2/22 why it returns ImageDTO, but byId returns Image itself?
    Page<ImageDTO> getAllImages(Pageable pageable);

    ResponseSettings deleteImageById(long id);
}
