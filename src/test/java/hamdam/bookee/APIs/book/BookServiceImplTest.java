package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.book.helpers.BookMappers;
import hamdam.bookee.APIs.book.helpers.BookRequestDTO;
import hamdam.bookee.APIs.book.helpers.BookResponseDTO;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.genre.GenreRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private BookServiceImpl underTest;

    @Test
    void addBook_shouldThrowExceptionWhenAuthorIdIsInvalid() {
        //given
        Long authorId = 1L;
        BookRequestDTO request = BookRequestDTO.builder().name("hobbit").authors(List.of(authorId)).build();
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.addBook(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(authorId.toString()
                );
    }

    @Test
    void addBook_shouldAddBookWhenRequestIsValid() {
        //given
        AppUserEntity author = AppUserEntity
                .builder()
                .name("name")
                .username("username")
                .password("pass")
                .build();
        author.setId(1L);
        List<AppUserEntity> authors = List.of(author);

        GenreEntity genre = new GenreEntity();
        genre.setId(2L);
        List<GenreEntity> genres = List.of(genre);

        BookRequestDTO request = new BookRequestDTO(
                "hobbit",
                "tagline",
                "desc",
                List.of(author.getId()),
                10.0,
                List.of(genre.getId())
        );
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(new AppUserEntity()));
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(new GenreEntity()));

        BookEntity book = BookMappers.mapToBookEntity(request, userRepository, genreRepository);
        book.setAuthors(authors);
        book.setGenres(genres);
        when(bookRepository.save(any())).thenReturn(book);

        //when
        BookResponseDTO actual = underTest.addBook(request);

        //then
        verify(bookRepository).save(any());
        assertThat(actual.getName()).isEqualTo(request.getName());
        assertThat(actual.getDescription()).isEqualTo(request.getDescription());
        assertThat(actual.getRating()).isEqualTo(request.getRating());
        assertThat(actual.getAuthors().size()).isEqualTo(request.getAuthors().size());
        assertThat(actual.getGenres().size()).isEqualTo(request.getGenres().size());
    }

    @Test
    void getAllBooks_shouldGetAllBooks() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        when(bookRepository.findAll(pageable)).thenReturn(Page.empty(Pageable.ofSize(5)));

        //when
        Page<BookResponseDTO> actual = underTest.getAllBooks(pageable);

        //then
        verify(bookRepository).findAll(pageable);
        assertThat(actual.getSize()).isEqualTo(pageable.getPageSize());
    }

    @Test
    void getBookById_throwsExceptionWhenIdIsInvalid() {
        //given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getBookById(bookId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(bookId.toString()
                );
    }

    @Test
    void getBookById_shouldGetBookById() {
        //given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new BookEntity()));

        //when
        BookResponseDTO actual = underTest.getBookById(bookId);

        //then
        verify(bookRepository).findById(bookId);
        assertThat(actual).isNotNull();
    }

    @Test
    void updateBook_throwsExceptionWhenBookIdIsInvalid() {
        //given
        Long bookId = 1L;
        BookRequestDTO request = new BookRequestDTO();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateBook(request, bookId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(bookId.toString()
                );
    }

    @Test
    void updateBook_throwsExceptionWhenGenreIdIsInvalid() {
        //given
        Long bookId = 1L;
        Long genreId = 2L;
        BookRequestDTO request = BookRequestDTO.builder()
                .name("hobbit")
                .rating(10.0)
                .authors(new ArrayList<>())
                .genres(List.of(genreId)).build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new BookEntity()));
        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateBook(request, bookId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(genreId.toString()
                );
    }

    @Test
    void updateBook_shouldUpdateBook() {
        //given
        Long bookId = 1L;
        Long genreId = 2L;
        BookRequestDTO request = BookRequestDTO.builder()
                .name("hobbit")
                .rating(10.0)
                .authors(new ArrayList<>())
                .genres(List.of(genreId)).build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new BookEntity()));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(new GenreEntity()));

        //when
        BookResponseDTO actual = underTest.updateBook(request, bookId);


        //then
        verify(bookRepository).findById(bookId);
        verify(genreRepository).findById(genreId);
        verify(bookRepository).save(any());
        assertThat(actual.getName()).isEqualTo(request.getName());
        assertThat(actual.getRating()).isEqualTo(request.getRating());
        assertThat(actual.getGenres().size()).isEqualTo(request.getGenres().size());
    }

    @Test
    void deleteBook_throwsExceptionWhenIdIsInvalid() {
        //given
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteBook(bookId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(bookId.toString()
                );
    }

    @Test
    void deleteBook_shouldDeleteBook() {
        //given
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        //when
        underTest.deleteBook(bookId);

        //then
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

}