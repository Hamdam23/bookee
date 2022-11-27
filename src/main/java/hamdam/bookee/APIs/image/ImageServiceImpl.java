package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.aws_s3.S3Repository;
import hamdam.bookee.APIs.image.helpers.FileUtils;
import hamdam.bookee.APIs.image.helpers.ImageDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3Repository s3Repository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public ImageEntity uploadImage(MultipartFile file) throws IOException {
        String fileName = FileUtils.getUniqueFileName(file);
        String location = s3Repository.writeFileToS3(file, bucketName, fileName);

        return imageRepository.save(new ImageEntity(fileName, location));
    }

    @Override
    public ImageDTO getImageByID(Long id) {
        return new ImageDTO(imageRepository.findById(id)
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
