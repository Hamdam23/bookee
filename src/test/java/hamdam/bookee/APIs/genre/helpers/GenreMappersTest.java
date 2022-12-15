package hamdam.bookee.APIs.genre.helpers;

import hamdam.bookee.APIs.genre.GenreEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class GenreMappersTest {

    @Test
    void mapToGenreResponseDTO_shouldReturnNullWhenRequestIsNull() {
        //given
        //when
        GenreResponseDTO actual = GenreMappers.mapToGenreResponseDTO(null);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToGenreResponseDTO_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        GenreEntity entity = GenreEntity.builder()
                .name("name")
                .description("desc")
                .timeStamp(LocalDateTime.now())
                .id(1L)
                .books(new ArrayList<>())
                .build();

        //when
        GenreResponseDTO actual = GenreMappers.mapToGenreResponseDTO(entity);

        //then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timeStamp", "id", "books")
                .isEqualTo(entity);
    }

    @Test
    void mapToGenreEntity_shouldReturnNullWhenRequestIsNull() {
        //given
        //when
        GenreEntity actual = GenreMappers.mapToGenreEntity(null);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToGenreEntity_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        GenreRequestDTO request = new GenreRequestDTO("name", "desc", null);

        //when
        GenreEntity actual = GenreMappers.mapToGenreEntity(request);

        //then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timeStamp", "id", "books")
                .isEqualTo(request);

    }

}