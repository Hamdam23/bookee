package hamdam.bookee.book;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BookEntity getBookById(Long id) {
        BookEntity book = bookRepository.findById(id).get();
        return book;
    }

    @Override
    public void addBook(BookEntity book) {
        bookRepository.save(book);
    }

    @Override
    public void updateBook(BookEntity book, Long id) {

        BookEntity oldBook = bookRepository.findById(id).get();

        if(oldBook == null){
            throw new ObjectNotFoundException(id, book.getName());
        }

        bookRepository.save(oldBook);
    }

    @Override
    public void deleteBook(Long id) {
        BookEntity oldBook = bookRepository.findById(id).get();

        if(oldBook == null){
            throw new ObjectNotFoundException(id, oldBook.getName());
        }

        bookRepository.deleteById(id);
    }
}
