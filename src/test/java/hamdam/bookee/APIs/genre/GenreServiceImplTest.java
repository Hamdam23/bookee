package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @InjectMocks
    private static GenreServiceImpl underTest;

    @Mock
    private static BookRepository bookRepository;

    @Mock
    private static GenreRepository genreRepository;

    @Test
    void shouldCreateRoleWhenValidRequestIsValid() {
        //given
        GenreDTO dto = new GenreDTO("name", "description");

        //when
        GenreDTO actual = underTest.addGenre(dto);

        //then
        assertThat(actual.getName()).isEqualTo(dto.getName());
        assertThat(actual.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    void shouldReturnEmptyDataWhenNoGenresAvailable() {
        //given
        when(genreRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());

        //when
        Page<GenreDTO> expected = underTest.getAllGenres(PageRequest.of(0, 5));

        //then
        assertThat(expected).isEmpty();
    }

    @Test
    void shouldReturnSingleDataWhenSingleGenreAvailable() {
        //given

        //when

        //then

    }

    @Test
    void shouldReturnMultipleRolesWhenMultipleGenresAvailable() {
        //given
        GenreEntity adventure = new GenreEntity("adventure", "adventure description");
        GenreEntity horror = new GenreEntity("horror", "horror description");
        Page<GenreEntity> actual = new PageImpl<>(List.of(adventure, horror));

        when(genreRepository.findAll(PageRequest.of(0, 5))).thenReturn(actual);

        //when
        Page<GenreDTO> expected = underTest.getAllGenres(PageRequest.of(0, 5));

        //then
        assertThat(actual.map(GenreDTO::new)).isEqualTo(expected);
    }

}