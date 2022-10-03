package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.book.BookRepository;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public GenreDTO addGenre(GenreDTO dto) {
        genreRepository.save(new GenreEntity(dto));
        return dto;
    }

    @Override
    public Page<GenreDTO> getAllGenres(Pageable pageable) {
        return genreRepository.findAll(pageable).map(GenreDTO::new);
    }

    @Override
    public GenreDTO getGenreById(Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        return new GenreDTO(genreEntity);
    }

    @Override
    public GenreDTO updateGenre(Long id, GenreDTO genreDTO) {
        GenreEntity oldGenre = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        BeanUtils.copyProperties(genreDTO, oldGenre, "id");

        // TODO: 9/2/22 why calling copyProperties?
        List<BookEntity> books = new ArrayList<>();
        genreDTO.getBooks().forEach(bookId -> {
            BookEntity book = bookRepository.findById(bookId).orElseThrow(()
                    -> new ResourceNotFoundException("Book", "id", bookId));
            books.add(book);
        });
        oldGenre.setBooks(books);

        genreRepository.save(oldGenre);
        return genreDTO;
    }

    @Override
    public ApiResponse deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre", "id", id);
        }
        genreRepository.deleteById(id);
        return new ApiResponse(
                HttpStatus.NO_CONTENT,
                LocalDateTime.now(),
                "Genre with id: " + id + " successfully deleted!"
        );
    }
}
