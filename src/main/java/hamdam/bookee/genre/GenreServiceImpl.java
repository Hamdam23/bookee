package hamdam.bookee.genre;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public GenreEntity getGenreByID(Long id) {
        return genreRepository.findById(id).get();
    }

    @Override
    public void addGenre(GenreEntity genre) {
        genreRepository.save(genre);
    }

    @Override
    public void updateGenre(Long id, GenreEntity newGenre) {
        GenreEntity genre = genreRepository.findById(id).get();

        genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
