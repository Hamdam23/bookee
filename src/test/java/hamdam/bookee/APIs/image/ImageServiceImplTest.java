package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.aws_s3.S3Repository;
import hamdam.bookee.APIs.image.helpers.ImageDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private S3Repository s3Repository;

    @InjectMocks
    private ImageServiceImpl underTest;

    private static final String bucketName = "very good bucketName";

    @Test
    void getUniqueFileName_shouldReturnValidDataWhenFileNameIsNull() {
        //given
        MultipartFile file = new MockMultipartFile("godzilla", ".png", "image/png", "godzilla".getBytes());

        //when
        String actual = underTest.getUniqueFileName(file);

        //then
        assertThat(actual).contains("image");
    }

    @Test
    void getUniqueFileName_shouldReturnValidDataWhenFileNameIsNotNull() {
        //given
        MultipartFile file = new MockMultipartFile("godzilla", "tes t.png", "image/png", "godzilla".getBytes());

        //when
        String actual = underTest.getUniqueFileName(file);

        //then
        assertThat(actual).contains("-");
    }

    @Test
    void uploadImage_returnsValidDataWhenFileNameNull() throws IOException {
        //given
        ReflectionTestUtils.setField(underTest, "bucketName", bucketName);
        MultipartFile file = new MockMultipartFile("godzilla", ".png", "image/png", "godzilla".getBytes());
        String url = bucketName + file.getOriginalFilename() + "image";
        when(s3Repository.writeFileToS3(any(), any(), any())).thenReturn(url);
        when(imageRepository.save(any())).thenReturn(new ImageEntity(file.getOriginalFilename(), url));

        //when
        ImageEntity actual = underTest.uploadImage(file);

        //then
        assertThat(actual.getLocation()).contains("image");
    }

    @Test
    void getImageByID_throwsExceptionWhenIdIsInvalid() {
        //given
        when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getImageByID(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getImageByID_returnValidDataWhenIdIsValid() {
        //given
        Long id = 1L;
        String name = "godzilla";
        String location = "movies/Narnia";
        ImageEntity image = new ImageEntity(name, location);
        image.setId(id);
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));

        //when
        ImageDTO actual = underTest.getImageByID(id);

        //then
        assertThat(actual.getImageName()).isEqualTo(name);
        assertThat(actual.getId()).isEqualTo(id);
    }

    @Test
    void deleteImageById_throwsExceptionWhenIdIsInvalid() {
        //given
        Long id = 1L;
        when(imageRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteImageById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(imageRepository).findById(id);
    }

    @Test
    void deleteImageById_returnValidResponseWhenIdIsValid() {
        //given
        Long id = 1L;
        ImageEntity image = new ImageEntity("name", "location");
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));

        //when
        ApiResponse actual = underTest.deleteImageById(id);

        //then
        verify(imageRepository).findById(id);
        verify(imageRepository).deleteById(id);
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}