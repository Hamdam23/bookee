package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.genre.helpers.GenreRequestDTO;
import hamdam.bookee.APIs.genre.helpers.GenreResponseDTO;
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
import static hamdam.bookee.tools.constants.Endpoints.API_GENRE;

@RestController
@RequestMapping(API_GENRE)
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    /**
     * The function takes a GenreRequestDTO object as a parameter, validates it, and then passes it to the
     * genreService.addGenre() function
     *
     * @param genre The genre object that we want to add to the database.
     * @return A GenreResponseDTO object
     */
    @PostMapping
    public GenreResponseDTO addGenre(@Valid @RequestBody GenreRequestDTO genre) {
        return genreService.addGenre(genre);
    }

    /**
     * It returns a paged response of genre response DTOs
     *
     * @param pageable This is a Spring Data interface that allows us to pass pagination parameters.
     * @return A list of all genres in the database.
     */
    @GetMapping
    public PagedResponse<GenreResponseDTO> getAllGenres(Pageable pageable) {
        return new PagedResponse<>(genreService.getAllGenres(pageable));
    }

    /**
     * It takes a Long id as a path variable, calls the genreService.getGenreById(id) function, and returns the
     * GenreResponseDTO object that the genreService.getGenreById(id) function returns
     *
     * @param id The id of the genre you want to get.
     * @return A GenreResponseDTO object
     */
    @GetMapping("/{id}")
    public GenreResponseDTO getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    /**
     * The function takes in a genre id and a genre object, and returns a genre response object
     *
     * @param id The id of the genre to be updated.
     * @param genre The genre object that will be updated.
     * @return A GenreResponseDTO object
     */
    @PatchMapping("/{id}")
    public GenreResponseDTO updateGenre(@PathVariable Long id, @Valid @RequestBody GenreRequestDTO genre) {
        return genreService.updateGenre(id, genre);
    }

    /**
     * It deletes a genre by id.
     *
     * @param id The id of the genre to be deleted.
     * @return A ResponseEntity object is being returned.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK,
                        LocalDateTime.now(),
                        getDeletionMessage("Genre", id)
                ), HttpStatus.OK
        );
    }
}
