package hamdam.bookee.APIs.genre;

import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public GenreEntity getGenreByID(Long id) {
        GenreEntity genre = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        return genre;
    }

    @Override
    public void addGenre(GenreDTO genre) {
        genreRepository.save(new GenreEntity(genre));
    }

    @Override
    public void updateGenre(Long id, GenreDTO genre) {
        GenreEntity oldGenre = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));

        GenreEntity newGenre = new GenreEntity(genre);

        oldGenre.setId(newGenre.getId());
        oldGenre.setName(newGenre.getName());
        oldGenre.setDescription(newGenre.getDescription());
        oldGenre.setBooks(newGenre.getBooks());
        genreRepository.save(oldGenre);
    }

    @Override
    public void deleteGenre(Long id) {
        genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        genreRepository.deleteById(id);
    }
}
