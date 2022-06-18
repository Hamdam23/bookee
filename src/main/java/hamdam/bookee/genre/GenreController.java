package hamdam.bookee.genre;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GenreController {

    private final GenreServiceImpl service;

    public GenreController(GenreServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/genres")
    public List<GenreEntity> getAllGenres(){
        return service.getAllGenres();
    }

    @GetMapping("/genre/{id}")
    public GenreEntity getGenreByID(@PathVariable Long id){
        return service.getGenreByID(id);
    }

    @PostMapping("/add/genre")
    public void addGenre(@RequestBody GenreEntity genre){
        service.addGenre(genre);
    }

    @PutMapping("/genre/{id}")
    public void updateGenre(@PathVariable Long id, @RequestBody GenreEntity genre){
        service.updateGenre(id, genre);
    }

    @DeleteMapping("/genre/{id}")
    public void deleteGenre(@PathVariable Long id){
        service.deleteGenre(id);
    }
}
