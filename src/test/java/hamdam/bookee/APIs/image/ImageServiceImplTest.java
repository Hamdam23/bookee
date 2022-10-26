package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.file.FileSystemRepository;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private FileSystemRepository fileSystemRepository;

    @InjectMocks
    private ImageServiceImpl underTest;

    private static final String imagesDirectory = System.getProperty("user.home");

    @Test
    void uploadImage_returnsValidDataWhenFileNameIsNotNull() throws IOException {
        //given
        ReflectionTestUtils.setField(underTest, "imagesDirectory", imagesDirectory);
        MultipartFile file = new MockMultipartFile("godzilla.png", "godzilla.png", "image/png", "godzilla".getBytes());
        String location = imagesDirectory + file.getOriginalFilename();
        when(fileSystemRepository.writeFilePath(any(), any())).thenReturn(location);
        when(imageRepository.save(any())).thenReturn(new ImageEntity(file.getOriginalFilename(), location));

        //when
        ImageEntity actual = underTest.uploadImage(file);

        //then
        assertThat(actual.getLocation()).contains(file.getOriginalFilename());
    }

    @Test
    void uploadImage_returnsValidDataWhenFileNameIsNull() throws IOException {
        //given
        ReflectionTestUtils.setField(underTest, "imagesDirectory", imagesDirectory);
        MultipartFile file = new MockMultipartFile(".png", ".png", "image/png", "godzilla".getBytes());
        String location = imagesDirectory + file.getOriginalFilename();
        when(fileSystemRepository.writeFilePath(any(), any())).thenReturn(location);
        when(imageRepository.save(any())).thenReturn(new ImageEntity(file.getOriginalFilename(), location));

        //when
        ImageEntity actual = underTest.uploadImage(file);

        //then
        assertThat(actual.getLocation()).contains(file.getOriginalFilename());
    }

    @Test
    void downloadImage_throwsExceptionWhenImageNameIsInvalid() {
        //given
        String godzilla = "godzilla";
        when(imageRepository.findByImageName(godzilla)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.downloadImage(godzilla))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(godzilla);
    }

    @Test
    void downloadImage_returnValidDataWhenRequestIsValid() throws IOException {
        //given
        String godzilla = "godzilla";
        ImageEntity image = new ImageEntity(godzilla, imagesDirectory + godzilla);
        Path path = Paths.get(image.getLocation());
        FileSystemResource expected = new FileSystemResource(path);
        when(imageRepository.findByImageName(godzilla)).thenReturn(Optional.of(image));
        when(fileSystemRepository.readFileFromPath(path)).thenReturn(expected);

        //when
        FileSystemResource actual = underTest.downloadImage(godzilla);

        //then
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
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
    void getAllImages_returnsEmptyDataWhenNoImageExists() {
        //given
        when(imageRepository.findAll(PageRequest.of(0, 10))).thenReturn(Page.empty());

        //when
        underTest.getAllImages(PageRequest.of(0, 10));

        //then
        verify(imageRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getAllImages_returnsValidDataWhenMultipleImagesExists() {
        //given
        Pageable pageable = PageRequest.of(0, 3);
        when(imageRepository.findAll(pageable))
                .thenReturn(Page.empty(Pageable.ofSize(3)));

        //when
        Page<ImageDTO> actual = underTest.getAllImages(pageable);

        //then
        verify(imageRepository).findAll(pageable);
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(actual.getSize());
    }

    @Test
    void deleteImageById_throwsExceptionWhenIdIsInvalid() {
        //given
        Long id = 1L;
        when(imageRepository.existsById(id)).thenReturn(false);

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteImageById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void deleteImageById_returnValidResponseWhenIdIsValid() {
        //given
        Long id = 1L;
        when(imageRepository.existsById(id)).thenReturn(true);

        //when
        ApiResponse actual = underTest.deleteImageById(id);

        //then
        verify(imageRepository).existsById(id);
        verify(imageRepository).deleteById(id);
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}