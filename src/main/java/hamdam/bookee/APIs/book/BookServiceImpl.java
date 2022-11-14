package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.book.helpers.BookDTO;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.genre.GenreRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AppUserRepository userRepository;

    /**
     * It takes a bookDTO, creates a bookEntity from it,
     * gets the authors and genres from the database,
     * and saves the bookEntity to the database
     *
     * @param book the book object that is passed in the request body
     * @return BookDTO
     */
    @Override
    public BookDTO addBook(BookDTO book) {
        BookEntity bookEntity = new BookEntity(book);
        List<AppUserEntity> authors = new ArrayList<>();
        // Getting the authors from the database and adding them to the bookEntity
        book.getAuthors().forEach(aLong -> {
            AppUserEntity author = userRepository.findById(aLong).orElseThrow(()
                    -> new ResourceNotFoundException("Author", "id", aLong));
            authors.add(author);
        });
        bookEntity.setAuthors(authors);

        List<GenreEntity> genres = getGenreEntities(book.getGenres());
        bookEntity.setGenres(genres);

        bookRepository.save(bookEntity);
        return book;
    }

    @Override
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookDTO::new);
    }

    @Override
    public BookDTO getBookById(Long id) {
        return new BookDTO(bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id)));
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO, Long id) {
        BookEntity oldBook = bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));
        BeanUtils.copyProperties(bookDTO, oldBook, "id");

        List<GenreEntity> genres = getGenreEntities(bookDTO.getGenres());
        oldBook.setGenres(genres);
        bookRepository.save(oldBook);

        return bookDTO;
    }

    @Override
    public ApiResponse deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", "id", id);
        }
        bookRepository.deleteById(id);

        return new ApiResponse(
                HttpStatus.NO_CONTENT,
                LocalDateTime.now(),
                "Book with id: " + id + " successfully deleted!"
        );
    }

    private List<GenreEntity> getGenreEntities(List<Long> genreIds) {
        List<GenreEntity> genreEntities = new ArrayList<>();
        genreIds.forEach(aLong -> {
            GenreEntity genre = genreRepository.findById(aLong).orElseThrow(()
                    -> new ResourceNotFoundException("Genre", "id", aLong));
            genreEntities.add(genre);
        });

        return genreEntities;
    }
}
