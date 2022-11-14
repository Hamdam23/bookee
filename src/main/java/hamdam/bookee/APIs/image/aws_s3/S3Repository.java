package hamdam.bookee.APIs.image.aws_s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;

@Component
@RequiredArgsConstructor
public class S3Repository {

    private final AmazonS3 amazonS3;

    public String writeFileToS3(MultipartFile file, String bucketName, String fileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(
                new PutObjectRequest(
                        bucketName, fileName, file.getInputStream(), metadata).withCannedAcl(PublicRead)
        );

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void deleteFile(String bucketName, String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }
}
