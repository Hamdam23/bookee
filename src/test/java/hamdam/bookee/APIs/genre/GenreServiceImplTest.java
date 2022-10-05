package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.book.BookRepository;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @InjectMocks
    private GenreServiceImpl underTest;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Test
    void addGenre_shouldCreateRoleWhenRequestIsValid() {
        //given
        GenreDTO dto = new GenreDTO("name", "description");

        //when
        GenreDTO actual = underTest.addGenre(dto);

        //then
        verify(genreRepository).save(new GenreEntity(dto));
        assertThat(actual.getName()).isEqualTo(dto.getName());
        assertThat(actual.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    void getAllGenres_shouldReturnSingleDataWhenSingleGenreAvailable() {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        when(genreRepository.findAll(pageable)).thenReturn(Page.empty(Pageable.ofSize(1)));

        //when
        Page<GenreDTO> actual = underTest.getAllGenres(pageable);

        //then
        verify(genreRepository).findAll(pageable);
        assertThat(pageable.getPageSize()).isEqualTo(actual.getSize());
    }

    @Test
    void getGenreById_throwExceptionWhenIdIsInvalid() {
        //given
        Long id = 1L;
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getGenreById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Genre")
                .hasMessageContaining("id")
                .hasMessageContaining(id.toString());
    }

    @Test
    void getGenreById_shouldReturnValidDataWhenIdIsValid() {
        //given
        Long id = 1L;
        when(genreRepository.findById(id)).thenReturn(Optional.of(new GenreEntity()));

        //when
        GenreDTO actual = underTest.getGenreById(id);

        //then
        verify(genreRepository).findById(id);
        assertThat(actual).isNotNull();
    }

    @Test
    void updateGenre_shouldThrowExceptionWhenIdIsInvalid() {
        //given
        Long id = 1L;
        GenreDTO genreDTO = new GenreDTO("adventure", "adventure description");
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateGenre(id, genreDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageStartingWith("Genre");
    }

    @Test
    void updateGenre_shouldReturnValidDataWhenRequestIsValid() {
        //given
        Long genreId = 1L;
        GenreDTO genreDTO = new GenreDTO("horror",
                "horror description",
                Arrays.asList(2L, 3L));
        GenreEntity genreEntity = new GenreEntity("adventure",
                "adventure description");
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genreEntity));
        when(bookRepository.findById(any())).thenReturn(Optional.of(new BookEntity()));

        //when
        GenreDTO actual = underTest.updateGenre(genreId, genreDTO);

        //then
        assertThat(genreDTO).isEqualTo(actual);
    }

    @Test
    void deleteGenre_shouldThrowExceptionWhenIdIsInvalid() {
        //given
        Long id = 1L;

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteGenre(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Genre")
                .hasMessageContaining("id")
                .hasMessageContaining(id.toString());
    }

    @Test
    void deleteGenre_shouldReturnApiResponseWhenIdIsValid() {
        //given
        Long genreId = 1L;
        when(genreRepository.existsById(genreId)).thenReturn(true);

        //when
        ApiResponse actual = underTest.deleteGenre(genreId);

        //then
        verify(genreRepository).deleteById(genreId);
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}