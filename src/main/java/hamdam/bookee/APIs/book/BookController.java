package hamdam.bookee.APIs.book;

import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok().body("Book successfully saved!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody BookDTO newBook) {
        bookService.updateBook(newBook, id);
        return ResponseEntity.ok().body("Book with id: " + id + " successfully updated!");
    }

    @GetMapping
    public List<BookEntity> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookEntity getBookByID(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().body("Book with id: " + id + " successfully deleted!");
    }
}

