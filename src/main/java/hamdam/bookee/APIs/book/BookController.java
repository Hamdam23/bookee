package hamdam.bookee.APIs.book;

import hamdam.bookee.tools.exeptions.ResponseSettings;
import hamdam.bookee.tools.paging.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hamdam.bookee.tools.constants.Endpoints.API_BOOK;

@RestController
@RequestMapping(API_BOOK)
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookService;

    @PostMapping
    public ResponseEntity<ResponseSettings> addBook(@RequestBody BookDTO book) {
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(bookService.addBook(book), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseSettings> updateBook(@PathVariable Long id, @RequestBody BookDTO newBook) {
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(bookService.updateBook(newBook, id), HttpStatus.OK);
    }

    @GetMapping
    public PagedResponse<BookEntity> getAllBooks(Pageable pageable) {
        return new PagedResponse<>(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    public BookEntity getBookByID(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSettings> deleteBook(@PathVariable Long id) {
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }
}

