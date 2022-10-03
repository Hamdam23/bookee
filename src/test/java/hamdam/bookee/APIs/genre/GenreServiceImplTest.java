package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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
    void shouldCreateRoleWhenRequestIsValid() {
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
        Pageable pageable = PageRequest.of(0, 5);
        when(genreRepository.findAll(any(Pageable.class))).thenReturn(Page.empty(Pageable.ofSize(5)));

        //when
        Page<GenreDTO> expected = underTest.getAllGenres(pageable);

        //then
        assertThat(expected).isEmpty();
    }

    @Test
    void shouldReturnSingleDataWhenSingleGenreAvailable() {
        //given
        List<GenreEntity> list = List.of(new GenreEntity("adventure", "adventure description"));
        Page<GenreEntity> actual = new PageImpl<>(
                list,
                PageRequest.of(1, 1),
                1);

        //when
        Page<GenreEntity> expected = new PageImpl<>(list, PageRequest.of(1, 1), 1);

        //then
        assertEquals(actual, expected);
    }

    @Test
    void shouldReturnMultipleRolesWhenMultipleGenresAvailable() {
        //given
        List<GenreEntity> list = new ArrayList<>();
        GenreEntity adventure = new GenreEntity("adventure", "adventure description");
        GenreEntity horror = new GenreEntity("horror", "horror description");
        list.add(adventure);
        list.add(horror);
        Page<GenreEntity> actual = new PageImpl<>(list, PageRequest.of(1, 3), 3);

        //when
        Page<GenreEntity> expected = new PageImpl<>(list, PageRequest.of(1, 3), 3);

        //then
        assertEquals(actual, expected);
    }

    @Test
    void throwExceptionWhenIdIsInvalid() {
        //given
        Long id = 1L;
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getGenreById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageStartingWith("Genre");
    }

    @Test
    void shouldReturnValidDataWhenIdIsValid() {
        //given
        Long id = 1L;
        when(genreRepository.findById(id)).thenReturn(Optional.of(new GenreEntity("adventure",
                "adventure description")));

        //when
        GenreDTO expected = underTest.getGenreById(id);

        //then
        assertThat(expected.getName()).isEqualTo("adventure");
        assertThat(expected.getDescription()).isEqualTo("adventure description");
    }

}