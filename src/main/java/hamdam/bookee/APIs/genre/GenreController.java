package hamdam.bookee.APIs.genre;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_GENRE;

@RestController
@RequestMapping(API_GENRE)
public class GenreController {

    private final GenreService service;

    public GenreController(GenreServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addGenre(@RequestBody GenreDTO genre) {
        service.addGenre(genre);
        return ResponseEntity.ok().body("Genre successfully saved!");
    }

    @GetMapping
    public List<GenreEntity> getAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreEntity getGenreByID(@PathVariable Long id) {
        return service.getGenreByID(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateGenre(@PathVariable Long id, @RequestBody GenreDTO genre) {
        service.updateGenre(id, genre);
        return ResponseEntity.ok().body("Genre with id: " + id + " successfully updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id) {
        service.deleteGenre(id);
        return ResponseEntity.ok().body("Genre with id: " + id + " successfully deleted!");
    }
}
