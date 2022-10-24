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

import static hamdam.bookee.tools.constants.Endpoints.API_GENRE;

@RestController
@RequestMapping(API_GENRE)
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public GenreResponseDTO addGenre(@Valid @RequestBody GenreRequestDTO genre) {
        return genreService.addGenre(genre);
    }

    @GetMapping
    public PagedResponse<GenreResponseDTO> getAllGenres(Pageable pageable) {
        return new PagedResponse<>(genreService.getAllGenres(pageable));
    }

    @GetMapping("/{id}")
    public GenreResponseDTO getGenreByID(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PatchMapping("/{id}")
    public GenreResponseDTO updateGenre(@PathVariable Long id, @Valid @RequestBody GenreRequestDTO genre) {
        genreService.updateGenre(id, genre);
        return genreService.updateGenre(id, genre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteGenre(@PathVariable Long id) {
        return new ResponseEntity<>(genreService.deleteGenre(id), HttpStatus.NO_CONTENT);
    }
}
