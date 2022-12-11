package hamdam.bookee.APIs.book.helpers;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.genre.GenreRepository;
import hamdam.bookee.APIs.genre.helpers.GenreMappers;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.helpers.UserMappers;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * It's a class that contains a bunch of methods that map a Book object to a BookDto object and vice versa.
 */

public class BookMappers {

    public static BookResponseDTO mapToBookResponse(BookEntity entity) {
        if (entity == null) return null;
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        BeanUtils.copyProperties(entity, bookResponseDTO);
        bookResponseDTO.setAuthors(entity.getAuthors().stream()
                .map(UserMappers::mapToAppUserResponseDTO)
                .collect(Collectors.toList()));
        bookResponseDTO.setGenres(entity.getGenres().stream()
                .map(GenreMappers::mapToGenreResponseDTO)
                .collect(Collectors.toList()));
        return bookResponseDTO;
    }

    public static BookEntity mapToBookEntity(
            BookRequestDTO bookRequestDTO,
            AppUserRepository userRepository,
            GenreRepository genreRepository
    ) {
        if (bookRequestDTO == null) return null;
        BookEntity bookEntity = new BookEntity();
        BeanUtils.copyProperties(bookRequestDTO, bookEntity, "id");

        bookEntity.setAuthors(getAuthors(bookRequestDTO.getAuthors(), userRepository));
        bookEntity.setGenres(getGenres(bookRequestDTO.getGenres(), genreRepository));

        return bookEntity;
    }

    public static List<AppUserEntity> getAuthors(List<Long> authorIds, AppUserRepository userRepository) {
        List<AppUserEntity> authors = new ArrayList<>();
        authorIds.forEach(aLong -> {
            AppUserEntity author = userRepository.findById(aLong).orElseThrow(()
                    -> new ResourceNotFoundException("Author", "id", aLong));
            authors.add(author);
        });

        return authors;
    }

    public static List<GenreEntity> getGenres(List<Long> genreIds, GenreRepository genreRepository) {
        List<GenreEntity> genreEntities = new ArrayList<>();
        genreIds.forEach(aLong -> {
            GenreEntity genre = genreRepository.findById(aLong).orElseThrow(()
                    -> new ResourceNotFoundException("Genre", "id", aLong));
            genreEntities.add(genre);
        });

        return genreEntities;
    }
}
