package hamdam.bookee.APIs.genre;

import java.util.List;

public interface GenreService {
    List<GenreEntity> getAllGenres();

    // TODO: 9/2/22 not ID, Id :-)
    GenreEntity getGenreByID(Long id);

    // TODO: 9/2/22 why void?
    void addGenre(GenreDTO genre);

    // TODO: 9/2/22 why void?
    void updateGenre(Long id, GenreDTO genre);

    void deleteGenre(Long id);
}
