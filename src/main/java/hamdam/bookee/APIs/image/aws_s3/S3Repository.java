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

    /**
     * It takes a file, a bucket name, and a file name, and returns a URL to the file on S3
     *
     * @param file The file that you want to upload to S3.
     * @param bucketName The name of the bucket you want to upload the file to.
     * @param fileName The name of the file that will be stored in S3.
     * @return The URL of the file on S3.
     */
    public String writeFileToS3(MultipartFile file, String bucketName, String fileName) throws IOException {
        // Creating a new ObjectMetadata object, setting the content length of the file,
        // and then uploading the file to S3.
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(
                new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata).withCannedAcl(PublicRead)
        );

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    /**
     * Delete a file from a bucket
     *
     * @param bucketName The name of the bucket where the file is stored.
     * @param fileName The name of the file to be deleted.
     */
    public void deleteFile(String bucketName, String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }
}
