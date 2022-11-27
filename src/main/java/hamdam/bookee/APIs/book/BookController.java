package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.book.helpers.BookRequestDTO;
import hamdam.bookee.APIs.book.helpers.BookResponseDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.paging.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDateTime;

import static hamdam.bookee.tools.constants.DeletionMessage.getDeletionMessage;
import static hamdam.bookee.tools.constants.Endpoints.API_BOOK;

@RestController
@RequestMapping(API_BOOK)
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookService;

    @PostMapping
    public BookResponseDTO addBook(@Valid @RequestBody BookRequestDTO book) {
        return bookService.addBook(book);
    }

    @GetMapping
    public PagedResponse<BookResponseDTO> getAllBooks(Pageable pageable) {
        return new PagedResponse<>(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PatchMapping("/{id}")
    public BookResponseDTO updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDTO newBook) {
        return bookService.updateBook(newBook, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK,
                        LocalDateTime.now(),
                        getDeletionMessage("Book", id)
                ), HttpStatus.OK
        );
    }
}

