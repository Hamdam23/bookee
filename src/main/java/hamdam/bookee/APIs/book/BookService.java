package hamdam.bookee.APIs.book;

import hamdam.bookee.tools.exeptions.ResponseSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

public interface BookService {
    // TODO: 9/2/22 why void
    ResponseSettings addBook(BookDTO book);

    Page<BookEntity> getAllBooks(Pageable pageable);

    BookEntity getBookById(Long id);

    // TODO: 9/2/22 why void
    ResponseSettings updateBook(BookDTO book, Long id);

    ResponseSettings deleteBook(Long id);
}
