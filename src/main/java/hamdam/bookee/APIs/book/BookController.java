package hamdam.bookee.APIs.book;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_BOOK;

@RestController
@RequestMapping(API_BOOK)
public class BookController {

    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookEntity> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookEntity getBookByID(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PostMapping
    public void addBook(@RequestBody BookEntity book){
        bookService.addBook(book);
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable Long id, @RequestBody BookEntity newBook){
        bookService.updateBook(newBook, id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }
}

