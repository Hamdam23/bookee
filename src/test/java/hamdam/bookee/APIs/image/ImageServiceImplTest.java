package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.file.FileSystemRepository;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private FileSystemRepository fileSystemRepository;

    private static final String imagesDirectory = "/home/hamdam/Pictures/bookee/";

    @InjectMocks
    private static ImageServiceImpl underTest;

    // TODO: 14/10/22 remove 'each' 
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "imagesDirectory", imagesDirectory);
    }

    @Test
    void uploadImage_returnsValidDataWhenRequestIsValid() throws IOException {
        //given
        MultipartFile file = new MockMultipartFile("godzilla.png", "godzilla".getBytes());
        // TODO: 14/10/22 why originalFilename is null 
        String location = imagesDirectory + file.getOriginalFilename();
        when(fileSystemRepository.writeFileToPath(any(), any())).thenReturn(location);
        when(imageRepository.save(any())).thenReturn(new ImageEntity(file.getOriginalFilename(), location));
        System.out.println(file.getOriginalFilename() + "sdfsdgdfgdfg");

        //when
        ImageEntity actual = underTest.uploadImage(file);

        //then
        // TODO: 14/10/22 proper assertion
        assertThat(actual.getLocation()).contains(file.getOriginalFilename());
    }

}