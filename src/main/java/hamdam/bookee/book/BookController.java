package hamdam.bookee.book;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1")
public class BookController {

    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<BookEntity> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/book/{id}")
    public BookEntity getBookByID(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PostMapping("/add")
    public void addBook(@RequestBody BookEntity book){
        bookService.addBook(book);
    }

    @PutMapping("/update/{id}")
    public void updateBook(@PathVariable Long id, @RequestBody BookEntity newBook){
        bookService.updateBook(newBook, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }
}

