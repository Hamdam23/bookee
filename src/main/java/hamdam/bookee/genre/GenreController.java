package hamdam.bookee.genre;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    private final GenreServiceImpl service;

    public GenreController(GenreServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public List<GenreEntity> getAllGenres(){
        return service.getAllGenres();
    }

    @GetMapping("{id}")
    public GenreEntity getGenreByID(@PathVariable Long id){
        return service.getGenreByID(id);
    }

    @PostMapping
    public void addGenre(@RequestBody GenreEntity genre){
        service.addGenre(genre);
    }

    @PutMapping("{id}")
    public void updateGenre(@PathVariable Long id, @RequestBody GenreEntity genre){
        service.updateGenre(id, genre);
    }

    @DeleteMapping("{id}")
    public void deleteGenre(@PathVariable Long id){
        service.deleteGenre(id);
    }
}
