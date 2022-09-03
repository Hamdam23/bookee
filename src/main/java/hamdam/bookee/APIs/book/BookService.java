package hamdam.bookee.APIs.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

public interface BookService {
    void addBook(BookDTO book);

    Page<BookEntity> getAllBooks(Pageable pageable);

    BookEntity getBookById(Long id);

    void updateBook(BookDTO book, Long id);

    void deleteBook(Long id);
}
