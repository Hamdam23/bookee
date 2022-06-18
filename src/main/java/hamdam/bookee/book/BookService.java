package hamdam.bookee.book;

import java.util.List;

public interface BookService {

    List<BookEntity> getAllBooks();
    BookEntity getBookById(Long id);
    void addBook(BookEntity book);
    void updateBook(BookEntity book, Long id);
    void deleteBook(Long id);

}
