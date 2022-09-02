package hamdam.bookee.APIs.genre;

import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
        // TODO: 9/2/22 Local variable 'genre' is redundant
        GenreEntity genre = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        return genre;
    }

    @Override
    public void addGenre(GenreDTO dto) {
        // TODO: 9/2/22 use GenreDTO as constructor argument
        GenreEntity entity = new GenreEntity();
        BeanUtils.copyProperties(dto, entity);
        genreRepository.save(entity);
    }

    @Override
    public void updateGenre(Long id, GenreDTO genre) {
        GenreEntity oldGenre = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));

        // TODO: 9/2/22 do yo really need 3 genre related object: oldGenre, newGenre, genre(DTO)?
        GenreEntity newGenre = new GenreEntity(genre);
        // TODO: 9/2/22 why calling copyProperties?
        BeanUtils.copyProperties(genre, oldGenre);

        oldGenre.setBooks(newGenre.getBooks());
        genreRepository.save(oldGenre);
    }

    @Override
    public void deleteGenre(Long id) {
        // TODO: 9/2/22 existsById is enough
        genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        genreRepository.deleteById(id);
    }
}
