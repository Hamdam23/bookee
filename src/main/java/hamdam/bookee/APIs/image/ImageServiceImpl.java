package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.file.FileSystemRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FileSystemRepository fileSystemRepository;

    @Value("${file_upload_path}")
    private String imagesDirectory;

    @Override
    public ImageEntity uploadImage(MultipartFile file) throws IOException {
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
        Path path = Paths.get(imagesDirectory + name);
        Files.createDirectories(path.getParent());
        String location = fileSystemRepository.writeFilePath(file.getBytes(), path);

        return imageRepository.save(new ImageEntity(name, location));
    }

    //TODO
    @Override
    public FileSystemResource downloadImage(String name) {
        ImageEntity imageEntity = imageRepository.findByImageName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "name", name));
        Path path = Paths.get(imageEntity.getLocation());
        return fileSystemRepository.readFileFromPath(path);
    }

    @Override
    public ImageDTO getImageByID(Long id) {
        return new ImageDTO(imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id)));
    }

    @Override
    public Page<ImageDTO> getAllImages(Pageable pageable) {
        return imageRepository.findAll(pageable).map(ImageDTO::new);
    }

    @Override
    public ApiResponse deleteImageById(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Image", "id", id);
        }

        imageRepository.deleteById(id);
        return new ApiResponse(
                HttpStatus.NO_CONTENT,
                LocalDateTime.now(),
                "Image with id: " + id + " successfully deleted!"
        );
    }
}
