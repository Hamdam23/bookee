package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.genre.GenreRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.ApiResponse;
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

    @Override
    public BookEntity addBook(BookDTO book) {
        BookEntity bookEntity = new BookEntity();
        List<AppUserEntity> authors = new ArrayList<>();
        book.getAuthors().forEach(aLong -> {
            AppUserEntity author = userRepository.findById(aLong).orElseThrow(()
                    -> new ResourceNotFoundException("Author", "id", aLong));
            authors.add(author);
        });
        bookEntity.setAuthors(authors);
        bookEntity = bookRepository.save(new BookEntity(book));
        return bookEntity;
    }

    @Override
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookDTO::new);
    }

    @Override
    public BookEntity getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));
    }

    // TODO: 9/2/22 see todos in GenreServiceImpl:updateGenre
    @Override
    public BookDTO updateBook(BookDTO bookDTO, Long id) {
        BookEntity oldBook = bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));
        BeanUtils.copyProperties(bookDTO, oldBook, "id");

        List<GenreEntity> genreEntities = new ArrayList<>();
        bookDTO.getGenres().forEach(aLong -> {
            GenreEntity genre = genreRepository.findById(id).orElseThrow(()
                    -> new ResourceNotFoundException("Genre", "id", aLong));
            genreEntities.add(genre);
        });
        oldBook.setGenres(genreEntities);
        bookRepository.save(oldBook);

        return bookDTO;
    }

    @Override
    public ApiResponse deleteBook(Long id) {
        // TODO: 9/2/22 existsById is enough
        if (!bookRepository.existsById(id)){
            throw new ResourceNotFoundException("Book", "id", id);
        }
        bookRepository.deleteById(id);

        return new ApiResponse(
                HttpStatus.NO_CONTENT,
                LocalDateTime.now(),
                "Book with id: " + id + " successfully deleted!"
        );
    }
}
