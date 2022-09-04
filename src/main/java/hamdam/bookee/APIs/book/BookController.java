package hamdam.bookee.APIs.book;

import hamdam.bookee.tools.paging.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_BOOK;

@RestController
@RequestMapping(API_BOOK)
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookService;

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody BookDTO book) {
        bookService.addBook(book);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Book successfully saved!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody BookDTO newBook) {
        bookService.updateBook(newBook, id);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Book with id: " + id + " successfully updated!");
    }

    @GetMapping
    public CustomPage<BookEntity> getAllBooks(Pageable pageable) {
        return new CustomPage<>(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    public BookEntity getBookByID(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Book with id: " + id + " successfully deleted!");
    }
}

