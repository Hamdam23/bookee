package hamdam.bookee.APIs.book.helpers;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.genre.GenreRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookMappersTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AppUserRepository userRepository;

    @Test
    void getGenreEntities_shouldReturnEmptyDataWhenGenreIdIsNull() {
        //given
        //when
        List<GenreEntity> actual = BookMappers.getGenres(new ArrayList<>(), genreRepository);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void getGenreEntities_shouldThrowExceptionWhenGenreIdIsInvalid() {
        //given
        Long id = 1L;
        List<Long> genreIds = List.of(id);
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> BookMappers.getGenres(genreIds, genreRepository))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(genreIds.toString()
                );
    }

    @Test
    void getGenreEntities_shouldReturnGenresWhenGenreIdIsValid() {
        //given
        Long adventure = 1L;
        Long horror = 2L;

        List<Long> genreIds = List.of(adventure, horror);
        when(genreRepository.findById(adventure)).thenReturn(Optional.of(new GenreEntity()));
        when(genreRepository.findById(horror)).thenReturn(Optional.of(new GenreEntity()));

        //when
        List<GenreEntity> actual = BookMappers.getGenres(genreIds, genreRepository);

        //then
        assertThat(actual.size()).isEqualTo(genreIds.size());
    }

    @Test
    void getAuthorEntities_shouldReturnEmptyDataWhenGenreIdIsNull() {
        //given
        //when
        List<AppUserEntity> actual = BookMappers.getAuthors(new ArrayList<>(), userRepository);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void getAuthorEntities_shouldThrowExceptionWhenAuthorIdIsInvalid() {
        //given
        Long id = 1L;
        List<Long> authorIds = List.of(id);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> BookMappers.getAuthors(authorIds, userRepository))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(authorIds.toString()
                );
    }

    @Test
    void getAuthorEntities_shouldReturnGenresWhenGenreIdIsValid() {
        //given
        Long bob = 1L;
        Long ted = 2L;

        List<Long> genreIds = List.of(bob, ted);
        when(userRepository.findById(bob)).thenReturn(Optional.of(new AppUserEntity()));
        when(userRepository.findById(ted)).thenReturn(Optional.of(new AppUserEntity()));

        //when
        List<AppUserEntity> actual = BookMappers.getAuthors(genreIds, userRepository);

        //then
        assertThat(actual.size()).isEqualTo(genreIds.size());
    }

    @Test
    void mapToBookResponse_shouldReturnNullWhenEntityIsNull() {
        //given
        //when
        BookResponseDTO actual = BookMappers.mapToBookResponse(null);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToBookResponse_shouldReturnValidDataWhenEntityIsValid() {
        //given
        BookEntity bookEntity = BookEntity.builder()
                .name("hobbit")
                .tagline("h-tag")
                .description("h-desc")
                .authors(new ArrayList<>())
                .rating(10.0)
                .genres(new ArrayList<>())
                .build();

        //when
        BookResponseDTO actual = BookMappers.mapToBookResponse(bookEntity);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(bookEntity);
    }

    @Test
    void mapToBookEntity_shouldReturnNullWhenRequestDtoIsNull() {
        //given
        //when
        BookEntity actual = BookMappers.mapToBookEntity(null, userRepository, genreRepository);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToBookEntity_shouldReturnValidDataWhenRequestDtoIsValid() {
        //given
        BookRequestDTO request = BookRequestDTO.builder()
                .name("hobbit")
                .tagline("h-tag")
                .description("h-desc")
                .authors(new ArrayList<>())
                .rating(10.0)
                .genres(new ArrayList<>())
                .build();

        //when
        BookEntity actual = BookMappers.mapToBookEntity(request, userRepository, genreRepository);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timeStamp", "id")
                .isEqualTo(request);
    }

}