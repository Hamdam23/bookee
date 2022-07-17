package hamdam.bookee.APIs.book;

import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public void addBook(BookDTO book) {
        bookRepository.save(new BookEntity(book));
    }

    @Override
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BookEntity getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));
    }

    @Override
    public void updateBook(BookDTO book, Long id) {
        BookEntity oldBook = bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));

        BeanUtils.copyProperties(book, oldBook);

        BookEntity newBook = new BookEntity(book);
        oldBook.setGenres(newBook.getGenres());
        bookRepository.save(oldBook);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Book", "id", id));

        bookRepository.deleteById(id);
    }
}
