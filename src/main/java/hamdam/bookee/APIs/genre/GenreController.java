package hamdam.bookee.APIs.genre;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_GENRE;

@RestController
@RequestMapping(API_GENRE)
public class GenreController {

    private final GenreServiceImpl service;

    public GenreController(GenreServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public List<GenreEntity> getAllGenres(){
        return service.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreEntity getGenreByID(@PathVariable Long id){
        return service.getGenreByID(id);
    }

    @PostMapping
    public void addGenre(@RequestBody GenreDTO genre){
        service.addGenre(genre);
    }

    @PutMapping("/{id}")
    public void updateGenre(@PathVariable Long id, @RequestBody GenreDTO genre){
        service.updateGenre(id, genre);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id){
        service.deleteGenre(id);
    }
}
