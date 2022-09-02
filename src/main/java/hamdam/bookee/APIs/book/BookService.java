package hamdam.bookee.APIs.book;

import java.util.List;

public interface BookService {
    // TODO: 9/2/22 why void
    void addBook(BookDTO book);

    List<BookEntity> getAllBooks();

    BookEntity getBookById(Long id);

    // TODO: 9/2/22 why void
    void updateBook(BookDTO book, Long id);

    void deleteBook(Long id);
}
