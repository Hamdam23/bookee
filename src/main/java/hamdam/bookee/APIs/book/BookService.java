package hamdam.bookee.APIs.book;

import java.util.List;

public interface BookService {

    List<BookEntity> getAllBooks();
    BookEntity getBookById(Long id);
    void addBook(BookDTO book);
    void updateBook(BookDTO book, Long id);
    void deleteBook(Long id);

}
