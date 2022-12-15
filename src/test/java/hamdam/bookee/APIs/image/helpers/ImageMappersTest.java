package hamdam.bookee.APIs.image.helpers;

import hamdam.bookee.APIs.image.ImageEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ImageMappersTest {

    @Test
    void mapToImageDTO_shouldReturnNullWhenRequestIsNull() {
        //given
        //when
        ImageDTO actual = ImageMappers.mapToImageDTO(null);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToImageDTO_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        ImageEntity image = ImageEntity.builder().imageName("name").url("url").build();

        //when
        ImageDTO actual = ImageMappers.mapToImageDTO(image);

        //then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timeStamp", "id")
                .isEqualTo(image);
    }

}