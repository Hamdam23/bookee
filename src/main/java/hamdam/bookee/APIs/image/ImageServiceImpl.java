package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.aws_s3.S3Repository;
import hamdam.bookee.APIs.image.helpers.FileUtils;
import hamdam.bookee.APIs.image.helpers.ImageDTO;
import hamdam.bookee.APIs.image.helpers.ImageMappers;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3Repository s3Repository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * It takes a file, generates a unique file name,
     * writes the file to S3,
     * and saves the file name and url to the database
     *
     * @param file The file that is being uploaded.
     * @return The image entity is being returned.
     */
    @Override
    public ImageEntity uploadImage(MultipartFile file) throws IOException {
        String fileName = FileUtils.getUniqueFileName(file);
        String url = s3Repository.writeFileToS3(file, bucketName, fileName);

        return imageRepository.save(ImageMappers.mapToImageEntity(fileName, url));
    }

    @Override
    public ImageDTO getImageByID(Long id) {
        return ImageMappers.mapToImageDTO(imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id)));
    }

    @Override
    public void deleteImageById(Long id) {
        ImageEntity image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));

        imageRepository.deleteById(id);
        s3Repository.deleteFile(bucketName, image.getImageName());
    }
}
