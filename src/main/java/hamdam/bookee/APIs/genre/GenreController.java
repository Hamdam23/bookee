package hamdam.bookee.APIs.genre;

import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.paging.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hamdam.bookee.tools.constants.Endpoints.API_GENRE;

@RestController
@RequestMapping(API_GENRE)
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public GenreDTO addGenre(@RequestBody GenreDTO genre) {
        // TODO: 9/2/22 return full json response
        return genreService.addGenre(genre);
    }

    @GetMapping
    public PagedResponse<GenreDTO> getAllGenres(Pageable pageable) {
        return new PagedResponse<>(genreService.getAllGenres(pageable));
    }

    @GetMapping("/{id}")
    public GenreDTO getGenreByID(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PatchMapping("/{id}")
    public GenreDTO updateGenre(@PathVariable Long id, @RequestBody GenreDTO genre) {
        genreService.updateGenre(id, genre);
        // TODO: 9/2/22 return full json response
        return genreService.updateGenre(id, genre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteGenre(@PathVariable Long id) {
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(genreService.deleteGenre(id), HttpStatus.NO_CONTENT);
    }
}
