package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.book.BookRepository;
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

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public GenreResponseDTO addGenre(GenreRequestDTO dto) {
        return new GenreResponseDTO(genreRepository.save(new GenreEntity(dto)));
    }

    @Override
    public Page<GenreResponseDTO> getAllGenres(Pageable pageable) {
        return genreRepository.findAll(pageable).map(GenreResponseDTO::new);
    }

    @Override
    public GenreResponseDTO getGenreById(Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        return new GenreResponseDTO(genreEntity);
    }

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

        return new GenreResponseDTO(genreRepository.save(oldGenre));
    }

    @Override
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre", "id", id);
        }
        genreRepository.deleteById(id);
    }
}
