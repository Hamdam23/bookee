package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.book.BookRepository;
import hamdam.bookee.APIs.genre.helpers.GenreMappers;
import hamdam.bookee.APIs.genre.helpers.GenreRequestDTO;
import hamdam.bookee.APIs.genre.helpers.GenreResponseDTO;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * It implements the GenreService interface
 *  and uses the GenreRepository to perform CRUD operations on the GenreEntity
 */
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    /**
     * It takes a GenreRequestDTO, maps it to a GenreEntity, saves it to the database, then maps it back to a
     * GenreResponseDTO and returns it
     *
     * @param dto The DTO that is passed in from the controller.
     * @return A GenreResponseDTO object
     */
    @Override
    public GenreResponseDTO addGenre(GenreRequestDTO dto) {
        return GenreMappers.mapToGenreResponseDTO(
                genreRepository.save(GenreMappers.mapToGenreEntity(dto))
        );
    }

    /**
     * It returns a page of GenreResponseDTO objects.
     *
     * @param pageable This is a Spring Data interface that allows pagination to be specified.
     * @return A Page of GenreResponseDTOs
     */
    @Override
    public Page<GenreResponseDTO> getAllGenres(Pageable pageable) {
        return genreRepository.findAll(pageable).map(GenreResponseDTO::new);
    }

    /**
     * It gets a genre by id.
     *
     * @param id The id of the genre to be retrieved.
     * @return A GenreResponseDTO object
     */
    @Override
    public GenreResponseDTO getGenreById(Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        return GenreMappers.mapToGenreResponseDTO(genreEntity);
    }

    /**
     * We're updating a genre by id, and we're updating it with the genreRequestDTO
     *
     * @param id The id of the genre to be updated.
     * @param genreRequestDTO The request body that contains the new values for the genre.
     * @return GenreResponseDTO
     */
    @Override
    public GenreResponseDTO updateGenre(Long id, GenreRequestDTO genreRequestDTO) {
        GenreEntity oldGenre = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        BeanUtils.copyProperties(genreRequestDTO, oldGenre, "id");

        List<BookEntity> books = new ArrayList<>();
        genreRequestDTO.getBooks().forEach(bookId -> {
            BookEntity book = bookRepository.findById(bookId).orElseThrow(()
                    -> new ResourceNotFoundException("Book", "id", bookId));
            books.add(book);
        });
        oldGenre.setBooks(books);

        return GenreMappers.mapToGenreResponseDTO(genreRepository.save(oldGenre));
    }

    /**
     * If the genre exists, delete it
     *
     * @param id The id of the genre to delete.
     */
    @Override
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre", "id", id);
        }
        genreRepository.deleteById(id);
    }
}
