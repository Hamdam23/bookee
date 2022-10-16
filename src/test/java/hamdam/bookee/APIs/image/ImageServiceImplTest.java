package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.file.FileSystemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private FileSystemRepository fileSystemRepository;

    @InjectMocks
    private static ImageServiceImpl underTest;

    private static final String imagesDirectory = "/home/hamdam/Pictures/bookee/";

    @Test
    void uploadImage_returnsValidDataWhenRequestIsValid() throws IOException {
        //given
        ReflectionTestUtils.setField(underTest, "imagesDirectory", imagesDirectory);
        MultipartFile file = new MockMultipartFile("godzilla.png", "godzilla.png", "image/png", "godzilla".getBytes());
        String location = imagesDirectory + file.getOriginalFilename();
        when(fileSystemRepository.writeFilePath(any(), any())).thenReturn(location);
        when(imageRepository.save(any())).thenReturn(new ImageEntity(file.getOriginalFilename(), location));

        //when
        ImageEntity actual = underTest.uploadImage(file);

        //then
        System.out.println(actual.getLocation());
        assertThat(actual.getLocation()).contains(file.getOriginalFilename());
    }

}