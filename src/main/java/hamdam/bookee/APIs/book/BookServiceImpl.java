package hamdam.bookee.APIs.book;

import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BookEntity getBookById(Long id) {
        BookEntity book = bookRepository.findById(id).orElseThrow(()
            -> new ResourceNotFoundException("Book", "id", id));
        return book;
    }

    @Override
    public void addBook(BookEntity book) {
        bookRepository.save(book);
    }

    @Override
    public void updateBook(BookEntity book, Long id) {
        BookEntity oldBok = bookRepository.findById(id).orElseThrow(()
            -> new ResourceNotFoundException("Book", "id", id));

        oldBok.setId(book.getId());
        oldBok.setName(book.getName());
        oldBok.setTagline(book.getTagline());
        oldBok.setDescription(book.getDescription());
        oldBok.setAuthor(book.getAuthor());
        oldBok.setGenres(book.getGenres());
        oldBok.setRating(book.getRating());
        bookRepository.save(oldBok);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));

        bookRepository.deleteById(id);
    }
}
