package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.book.BookRepository;
import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import hamdam.bookee.tools.exeptions.ResponseSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public GenreEntity getGenreById(Long id) {
        // TODO: 9/2/22 Local variable 'genre' is redundant
        return genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
    }

    @Override
    public ResponseSettings addGenre(GenreDTO dto) {
        // TODO: 9/2/22 use GenreDTO as constructor argument
        GenreEntity entity = new GenreEntity(dto);
        genreRepository.save(entity);
        return new ResponseSettings(
                HttpStatus.OK,
                LocalDateTime.now(),
                "Genre successfully saved!"
        );
    }

    @Override
    public ResponseSettings updateGenre(Long id, GenreDTO genre) {
        GenreEntity oldGenre = genreRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Genre", "id", id));
        BeanUtils.copyProperties(genre, oldGenre);

        // TODO: 9/2/22 do yo really need 3 genre related object: oldGenre, newGenre, genre(DTO)?
        // TODO: 9/2/22 why calling copyProperties?
        List<BookEntity> books = new ArrayList<>();
        genre.getBooks().forEach(aLong -> {
            BookEntity book = bookRepository.findById(id).orElseThrow(()
                    -> new ResourceNotFoundException("Book", "id", aLong));
            books.add(book);
        });
        oldGenre.setBooks(books);

        genreRepository.save(oldGenre);
        return new ResponseSettings(
                HttpStatus.OK,
                LocalDateTime.now(),
                "Genre with id: " + id + " successfully updated!"
        );
    }

    @Override
    public ResponseSettings deleteGenre(Long id) {
        // TODO: 9/2/22 existsById is enough
        genreRepository.existsById(id);
        genreRepository.deleteById(id);
        return new ResponseSettings(
                HttpStatus.OK,
                LocalDateTime.now(),
                "Genre with id: " + id + " successfully deleted!"
        );
    }
}
