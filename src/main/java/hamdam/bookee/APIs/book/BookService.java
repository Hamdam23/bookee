package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.book.helpers.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDTO addBook(BookDTO book);

    Page<BookDTO> getAllBooks(Pageable pageable);

    BookDTO getBookById(Long id);

    BookDTO updateBook(BookDTO book, Long id);

    void deleteBook(Long id);
}
