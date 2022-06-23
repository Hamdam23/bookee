package hamdam.bookee.APIs.genre;

import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;

    @Override
    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public GenreEntity getGenreByID(Long id) {
        GenreEntity genre  = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        return genre;
    }

    @Override
    public void addGenre(GenreEntity genre) {
        genreRepository.save(genre);
    }

    @Override
    public void updateGenre(Long id, GenreEntity genre) {
        GenreEntity oldGenre  = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));

        oldGenre.setId(genre.getId());
        oldGenre.setName(genre.getName());
        oldGenre.setDescription(genre.getDescription());
        oldGenre.setBooks(genre.getBooks());
        genreRepository.save(oldGenre);
    }

    @Override
    public void deleteGenre(Long id) {
        genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        genreRepository.deleteById(id);
    }
}
