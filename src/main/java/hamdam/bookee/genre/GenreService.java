package hamdam.bookee.genre;

import java.util.List;

public interface GenreService {

    List<GenreEntity> getAllGenres();
    GenreEntity getGenreByID(Long id);
    void addGenre(GenreEntity genre);
    void updateGenre(Long id, GenreEntity genre);
    void deleteGenre(Long id);
}
