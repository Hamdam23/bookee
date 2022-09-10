package hamdam.bookee.APIs.genre;

import hamdam.bookee.tools.exeptions.ResponseSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {
    Page<GenreEntity> getAllGenres(Pageable pageable);

    // TODO: 9/2/22 not ID, Id :-)
    GenreEntity getGenreById(Long id);

    // TODO: 9/2/22 why void?
    ResponseSettings addGenre(GenreDTO genre);

    // TODO: 9/2/22 why void?
    ResponseSettings updateGenre(Long id, GenreDTO genre);

    ResponseSettings deleteGenre(Long id);
}
