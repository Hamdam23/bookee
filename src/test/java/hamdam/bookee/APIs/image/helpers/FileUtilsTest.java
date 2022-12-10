package hamdam.bookee.APIs.image.helpers;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileUtilsTest {

    @Test
    void getUniqueFileName_shouldReturnValidDataWhenFileNameIsNull() {
        //given
        MultipartFile file = new MockMultipartFile("godzilla", ".png", "image/png", "godzilla".getBytes());

        //when
        String actual = FileUtils.getUniqueFileName(file);

        //then
        assertThat(actual).contains("image");
    }

    @Test
    void getUniqueFileName_shouldReturnValidDataWhenFileNameIsNotNull() {
        //given
        MultipartFile file = new MockMultipartFile("godzilla", "tes t.png", "image/png", "godzilla".getBytes());

        //when
        String actual = FileUtils.getUniqueFileName(file);

        //then
        assertThat(actual).contains("-");
    }
}