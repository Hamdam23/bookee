package hamdam.bookee.APIs.genre;

import java.util.List;

public interface GenreService {

    List<GenreEntity> getAllGenres();
    GenreEntity getGenreByID(Long id);
    void addGenre(GenreDTO genre);
    void updateGenre(Long id, GenreDTO genre);
    void deleteGenre(Long id);
}
