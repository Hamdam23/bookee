package hamdam.bookee.APIs.book;

import java.util.List;

public interface BookService {
    void addBook(BookDTO book);

    List<BookEntity> getAllBooks();

    BookEntity getBookById(Long id);

    void updateBook(BookDTO book, Long id);

    void deleteBook(Long id);
}
