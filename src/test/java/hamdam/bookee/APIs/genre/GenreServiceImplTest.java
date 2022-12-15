package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.book.BookRepository;
import hamdam.bookee.APIs.genre.helpers.GenreMappers;
import hamdam.bookee.APIs.genre.helpers.GenreRequestDTO;
import hamdam.bookee.APIs.genre.helpers.GenreResponseDTO;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
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
        GenreRequestDTO dto = new GenreRequestDTO("name", "desc", null);
        when(genreRepository.save(any())).thenReturn(GenreMappers.mapToGenreEntity(dto));

        //when
        GenreResponseDTO actual = underTest.addGenre(dto);

        //then
        verify(genreRepository).save(any());
        assertThat(actual.getName()).isEqualTo(dto.getName());
        assertThat(actual.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    void getAllGenres_shouldReturnSingleDataWhenSingleGenreAvailable() {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        when(genreRepository.findAll(pageable)).thenReturn(Page.empty(Pageable.ofSize(1)));

        //when
        Page<GenreResponseDTO> actual = underTest.getAllGenres(pageable);

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
        GenreResponseDTO actual = underTest.getGenreById(id);

        //then
        verify(genreRepository).findById(id);
        assertThat(actual).isNotNull();
    }

    @Test
    void updateGenre_shouldThrowExceptionWhenIdIsInvalid() {
        //given
        Long id = 1L;
        GenreRequestDTO genreResponseDTO = new GenreRequestDTO("name", "desc", null);
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateGenre(id, genreResponseDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void updateGenre_throwsExceptionWhenBookIdIsInvalid() {
        //given
        Long id = 1L;
        GenreRequestDTO genreResponseDTO = new GenreRequestDTO("adventure",
                "adventure description",
                List.of(2L)
        );
        when(genreRepository.findById(id)).thenReturn(Optional.of(new GenreEntity()));
        when(bookRepository.findById(genreResponseDTO.getBooks().get(0))).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateGenre(id, genreResponseDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(genreResponseDTO.getBooks().get(0)))
                .hasMessageContaining(genreResponseDTO.getBooks().get(0).toString());
    }

    @Test
    void updateGenre_shouldReturnValidDataWhenRequestIsValid() {
        //given
        Long genreId = 1L;
        BookEntity hobbit = new BookEntity();
        hobbit.setId(2L);
        BookEntity naruto = new BookEntity();
        naruto.setId(3L);
        GenreRequestDTO genreRequestDTO = new GenreRequestDTO("horror",
                "horror description",
                Arrays.asList(hobbit.getId(), naruto.getId()));
        GenreEntity genreEntity = GenreEntity.builder().name("name").description("desc").build();

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genreEntity));
        when(bookRepository.findById(hobbit.getId())).thenReturn(Optional.of(hobbit));
        when(bookRepository.findById(naruto.getId())).thenReturn(Optional.of(naruto));
        when(genreRepository.save(genreEntity)).thenReturn(genreEntity);

        //when
        GenreResponseDTO actual = underTest.updateGenre(genreId, genreRequestDTO);

        //then
        assertThat(actual.getName()).isEqualTo(genreRequestDTO.getName());
        assertThat(actual.getDescription()).isEqualTo(genreRequestDTO.getDescription());
        assertThat(actual.getBooks()).isEqualTo(genreRequestDTO.getBooks());
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
        underTest.deleteGenre(genreId);

        //then
        verify(genreRepository).deleteById(genreId);
    }
}