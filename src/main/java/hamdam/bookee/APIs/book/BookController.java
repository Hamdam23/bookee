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

    /**
     * The function takes a BookRequestDTO object as a parameter, validates it, and then passes it to the
     * bookService.addBook() function
     *
     * @param book The book object that we want to add to the database.
     * @return BookResponseDTO
     */
    @PostMapping
    public BookResponseDTO addBook(@Valid @RequestBody BookRequestDTO book) {
        return bookService.addBook(book);
    }

    /**
     * It returns a paged response of book response DTOs
     *
     * @param pageable This is a Spring Data interface that allows us to pass pagination parameters.
     * @return A PagedResponse object containing a list of BookResponseDTO objects.
     */
    @GetMapping
    public PagedResponse<BookResponseDTO> getAllBooks(Pageable pageable) {
        return new PagedResponse<>(bookService.getAllBooks(pageable));
    }

    /**
     * It takes a Long id as a path variable, and returns a BookResponseDTO
     *
     * @param id The id of the book to be retrieved.
     * @return BookResponseDTO
     */
    @GetMapping("/{id}")
    public BookResponseDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    /**
     * The function takes in a new book object and an id, and returns a book response object
     *
     * @param id The id of the book to be updated.
     * @param newBook The new book object that will be used to update the existing book.
     * @return BookResponseDTO
     */
    @PatchMapping("/{id}")
    public BookResponseDTO updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDTO newBook) {
        return bookService.updateBook(newBook, id);
    }

    /**
     * It deletes a book with the given id and returns a response entity with a status code of 200 and a message saying
     * that the book was deleted
     *
     * @param id The id of the book to be deleted
     * @return A ResponseEntity object is being returned.
     */
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

