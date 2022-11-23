package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.book.helpers.BookRequestDTO;
import hamdam.bookee.APIs.book.helpers.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDTO addBook(BookRequestDTO book);

    Page<BookResponseDTO> getAllBooks(Pageable pageable);

    BookResponseDTO getBookById(Long id);

    BookResponseDTO updateBook(BookRequestDTO book, Long id);

    void deleteBook(Long id);
}
