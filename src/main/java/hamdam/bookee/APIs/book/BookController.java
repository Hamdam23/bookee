package hamdam.bookee.APIs.book;

import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.paging.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static hamdam.bookee.tools.constants.Endpoints.API_BOOK;

@RestController
@RequestMapping(API_BOOK)
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookService;

    @PostMapping
    public BookEntity addBook(@Valid @RequestBody BookDTO book) {
        // TODO: 9/2/22 return full json response
        return bookService.addBook(book);
    }

    @GetMapping
    public PagedResponse<BookDTO> getAllBooks(Pageable pageable) {
        return new PagedResponse<>(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    public BookEntity getBookByID(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PatchMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO newBook) {
        // TODO: 9/2/22 return full json response
        return bookService.updateBook(newBook, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) {
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.NO_CONTENT);
    }
}

