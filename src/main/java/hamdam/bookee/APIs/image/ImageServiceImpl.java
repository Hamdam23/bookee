package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.file.FileSystemRepository;
import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;
    private final FileSystemRepository fileSystemRepository;

    @Override
    public Image getImageByID(long id) {
        Image image = imageRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Image", "id", id));
        return image;
    }

    @Override
    public Image uploadImage(MultipartFile file) throws Exception {
        String fileNameWithOutExt = FilenameUtils.removeExtension(file.getOriginalFilename()).replace(" ", "-");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = fileNameWithOutExt + "-" + new Date().getTime() + "." + extension;
        String location = fileSystemRepository.writeFile(file.getBytes(), name);

        return imageRepository.save(new Image(name, location));
    }

    @Override
    public FileSystemResource downloadImage(String name) {
        Image image = imageRepository.findImageByImageName(name).orElseThrow(()
            -> new RuntimeException("Image with name: " + name + " not found")
        );
        return fileSystemRepository.readFile(image.getLocation());
    }
}
