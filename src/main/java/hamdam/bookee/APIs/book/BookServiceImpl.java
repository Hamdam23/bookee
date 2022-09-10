package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.genre.GenreRepository;
import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import hamdam.bookee.tools.exeptions.ResponseSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @Override
    public ResponseSettings addBook(BookDTO book) {
        bookRepository.save(new BookEntity(book));
        return new ResponseSettings(
                HttpStatus.OK,
                LocalDateTime.now(),
                book.getName() + " book successfully saved!"
        );
    }

    @Override
    public Page<BookEntity> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public BookEntity getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));
    }

    // TODO: 9/2/22 see todos in GenreServiceImpl:updateGenre
    @Override
    public ResponseSettings updateBook(BookDTO book, Long id) {
        BookEntity oldBook = bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));
        BeanUtils.copyProperties(book, oldBook);

        List<GenreEntity> genreEntities = new ArrayList<>();
        book.getGenres().forEach(aLong -> {
            GenreEntity genre = genreRepository.findById(id).orElseThrow(()
                    -> new ResourceNotFoundException("Genre", "id", aLong));
            genreEntities.add(genre);
        });

        oldBook.setGenres(genreEntities);
        bookRepository.save(oldBook);

        return new ResponseSettings(
                HttpStatus.OK,
                LocalDateTime.now(),
                "Book with id: " + id + " successfully updated!"
        );
    }

    @Override
    public ResponseSettings deleteBook(Long id) {
        // TODO: 9/2/22 existsById is enough
        bookRepository.existsById(id);
        bookRepository.deleteById(id);

        return new ResponseSettings(
                HttpStatus.OK,
                LocalDateTime.now(),
                "Book with id: " + id + " successfully deleted!"
        );
    }
}
