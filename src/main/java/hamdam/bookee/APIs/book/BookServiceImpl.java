package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.book.helpers.BookMappers;
import hamdam.bookee.APIs.book.helpers.BookRequestDTO;
import hamdam.bookee.APIs.book.helpers.BookResponseDTO;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.genre.GenreRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static hamdam.bookee.APIs.book.helpers.BookMappers.getAuthors;
import static hamdam.bookee.APIs.book.helpers.BookMappers.getGenres;

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
    public BookResponseDTO addBook(BookRequestDTO book) {
        BookEntity bookEntity = BookMappers.mapToBookEntity(book, userRepository, genreRepository);

        return BookMappers.mapToBookResponse(bookRepository.save(bookEntity));
    }

    /**
     * It returns a page of BookResponseDTO objects.
     *
     * @param pageable This is the pageable object that contains the page number, page size, and sort order.
     * @return A Page of BookResponseDTOs
     */
    @Override
    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookMappers::mapToBookResponse);
    }

    /**
     * It returns a book by id.
     *
     * @param id The id of the book to be retrieved.
     * @return BookResponseDTO
     */
    @Override
    public BookResponseDTO getBookById(Long id) {
        return BookMappers.mapToBookResponse(bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id)));
    }

    /**
     * We are updating the book with the id passed in the request body
     *
     * @param bookRequest The request body that contains the new book details.
     * @param id          The id of the book to be updated.
     * @return BookResponseDTO
     */
    @Override
    public BookResponseDTO updateBook(BookRequestDTO bookRequest, Long id) {
        BookEntity oldBook = bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));
        bookRepository.save(BookMappers.mapToBookEntity(bookRequest, oldBook, userRepository, genreRepository));

        return BookMappers.mapToBookResponse(oldBook);
    }

    /**
     * If the book with the given id doesn't exist, throw a ResourceNotFoundException. Otherwise, delete the book with the
     * given id
     *
     * @param id The id of the book to delete.
     */
    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", "id", id);
        }
        bookRepository.deleteById(id);
    }

}
