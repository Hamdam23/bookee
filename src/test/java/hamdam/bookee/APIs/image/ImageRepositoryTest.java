package hamdam.bookee.APIs.image;

import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    private ImageRepository underTest;

    @BeforeEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByImageName_returnEmptyDataWhenNoImageByName() {
        //given
        //when
        Optional<ImageEntity> actual = underTest.findByImageName("name");

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void findByImageName_returnValidDataWhenImageNameIsValid() {
        //given
        String name = "name123456.jpg";
        ImageEntity expected = underTest.save(new ImageEntity(name, "location"));

        //when
        Optional<ImageEntity> actual = underTest.findByImageName(name);

        //then
        ImageEntity image = actual.orElseThrow(() -> new ResourceNotFoundException("Image", "name", name));
        assertThat(image.getImageName()).isEqualTo(expected.getImageName());
    }

}