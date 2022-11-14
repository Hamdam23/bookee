package hamdam.bookee.APIs.image.aws_s3;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3RepositoryTest {

    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private S3Repository underTest;

    @Test
    void writeFileToS3_shouldReturnValidResponseWhenRequestIsValid() throws IOException {
        //given
        String fileName = "book";
        String bucketName = "bucket";
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName.getBytes());

        String urlStr = "https://test.com?test=" + encode("test\\", UTF_8);
        URL url = new URL(urlStr);
        when(amazonS3.getUrl(bucketName, fileName)).thenReturn(url);

        //when
        String actual = underTest.writeFileToS3(multipartFile, bucketName, fileName);

        //then
        assertThat(actual).isEqualTo(url.toString());
    }

    @Test
    void deleteFile_shouldDeleteFileWhenRequestIsValid() {
        //given
        String fileName = "book";
        String bucketName = "bucket";

        //when
        underTest.deleteFile(bucketName, fileName);

        //then
        verify(amazonS3).deleteObject(bucketName, fileName);
    }
}