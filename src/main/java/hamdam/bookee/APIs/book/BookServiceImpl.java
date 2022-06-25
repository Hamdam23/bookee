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
    public void addBook(BookDTO book) {
        bookRepository.save(new BookEntity(book));
    }

    @Override
    public void updateBook(BookDTO book, Long id) {
        BookEntity oldBok = bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));

        BookEntity newBook = new BookEntity(book);

        oldBok.setId(newBook.getId());
        oldBok.setName(newBook.getName());
        oldBok.setTagline(newBook.getTagline());
        oldBok.setDescription(newBook.getDescription());
        oldBok.setAuthor(newBook.getAuthor());
        oldBok.setGenres(newBook.getGenres());
        oldBok.setRating(newBook.getRating());
        bookRepository.save(oldBok);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));

        bookRepository.deleteById(id);
    }
}
