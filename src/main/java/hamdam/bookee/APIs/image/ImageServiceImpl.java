package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.file.FileSystemRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final FileSystemRepository fileSystemRepository;

    @Override
    public ImagEntity uploadImage(MultipartFile file) throws Exception {
        String fileNameWithoutExt = FilenameUtils.removeExtension(file.getOriginalFilename());
        if (fileNameWithoutExt == null) {
            fileNameWithoutExt = "";
        } else {
            fileNameWithoutExt = fileNameWithoutExt.replace(" ", "-");
        }
        //TODO check for null and empty
        // isn't it get handled by custom ValidFile annotation?
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = fileNameWithoutExt + "-" + new Date().getTime() + "." + extension;
        String location = fileSystemRepository.writeFile(file.getBytes(), name);

        return imageRepository.save(new ImagEntity(name, location));
    }

    @Override
    public FileSystemResource downloadImage(String name) {
        ImagEntity imagEntity = imageRepository.findByImageName(name).orElseThrow(()
                // TODO: 9/2/22 custom exception
                -> new ResourceNotFoundException("Image", "name", name)
        );
        return fileSystemRepository.readFile(imagEntity.getLocation());
    }

    @Override
    public ImageDTO getImageByID(long id) {
        return new ImageDTO(imageRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Image", "id", id))
        );
    }

    @Override
    public Page<ImageDTO> getAllImages(Pageable pageable) {
        return imageRepository.findAll(pageable).map(ImageDTO::new);
    }

    @Override
    public ApiResponse deleteImageById(long id) {
        // TODO: 9/2/22 check image id
        imageRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Image", "id", id));
        imageRepository.deleteById(id);
        return new ApiResponse(
                HttpStatus.OK,
                LocalDateTime.now(),
                "Image with id: " + id + " successfully deleted!"
        );
    }
}
