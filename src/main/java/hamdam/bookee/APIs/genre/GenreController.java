package hamdam.bookee.APIs.genre;

import hamdam.bookee.tools.exeptions.ResponseSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_GENRE;

@RestController
@RequestMapping(API_GENRE)
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<ResponseSettings> addGenre(@RequestBody GenreDTO genre) {
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(genreService.addGenre(genre), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseSettings> updateGenre(@PathVariable Long id, @RequestBody GenreDTO genre) {
        genreService.updateGenre(id, genre);
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(genreService.updateGenre(id, genre), HttpStatus.OK);
    }

    @GetMapping
    public List<GenreEntity> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreEntity getGenreByID(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSettings> deleteGenre(@PathVariable Long id) {
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(genreService.deleteGenre(id), HttpStatus.OK);
    }
}
