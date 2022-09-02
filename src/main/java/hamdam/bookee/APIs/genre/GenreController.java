package hamdam.bookee.APIs.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_GENRE;

@RestController
@RequestMapping(API_GENRE)
@RequiredArgsConstructor
public class GenreController {

    private final GenreService service;

    @PostMapping
    public ResponseEntity<String> addGenre(@RequestBody GenreDTO genre) {
        service.addGenre(genre);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Genre successfully saved!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateGenre(@PathVariable Long id, @RequestBody GenreDTO genre) {
        service.updateGenre(id, genre);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Genre with id: " + id + " successfully updated!");
    }

    @GetMapping
    public List<GenreEntity> getAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreEntity getGenreByID(@PathVariable Long id) {
        return service.getGenreByID(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id) {
        service.deleteGenre(id);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Genre with id: " + id + " successfully deleted!");
    }
}
