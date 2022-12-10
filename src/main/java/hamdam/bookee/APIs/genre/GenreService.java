package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.genre.helpers.GenreRequestDTO;
import hamdam.bookee.APIs.genre.helpers.GenreResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {

    GenreResponseDTO addGenre(GenreRequestDTO genre);

    Page<GenreResponseDTO> getAllGenres(Pageable pageable);

    GenreResponseDTO getGenreById(Long id);

    GenreResponseDTO updateGenre(Long id, GenreRequestDTO genre);

    void deleteGenre(Long id);
}
