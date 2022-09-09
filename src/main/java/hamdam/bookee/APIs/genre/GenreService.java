package hamdam.bookee.APIs.genre;

import hamdam.bookee.tools.exeptions.ResponseSettings;

import java.util.List;

public interface GenreService {
    List<GenreEntity> getAllGenres();

    // TODO: 9/2/22 not ID, Id :-)
    GenreEntity getGenreById(Long id);

    // TODO: 9/2/22 why void?
    ResponseSettings addGenre(GenreDTO genre);

    // TODO: 9/2/22 why void?
    ResponseSettings updateGenre(Long id, GenreDTO genre);

    ResponseSettings deleteGenre(Long id);
}
