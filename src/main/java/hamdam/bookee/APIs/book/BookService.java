package hamdam.bookee.APIs.book;

import hamdam.bookee.tools.exceptions.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    // TODO: 9/2/22 why void
    BookEntity addBook(BookDTO book);

    Page<BookDTO> getAllBooks(Pageable pageable);

    BookEntity getBookById(Long id);

    // TODO: 9/2/22 why void
    BookDTO updateBook(BookDTO book, Long id);

    ApiResponse deleteBook(Long id);
}
