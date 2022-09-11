package hamdam.bookee.APIs.genre;

import hamdam.bookee.tools.exeptions.ResponseSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {

    // TODO: 9/2/22 why void?
    GenreDTO addGenre(GenreDTO genre);

    Page<GenreDTO> getAllGenres(Pageable pageable);

    // TODO: 9/2/22 not ID, Id :-)
    GenreDTO getGenreById(Long id);

    // TODO: 9/2/22 why void?
    GenreDTO updateGenre(Long id, GenreDTO genre);

    ResponseSettings deleteGenre(Long id);
}
