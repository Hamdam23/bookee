package hamdam.bookee.APIs.genre.helpers;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.GenreEntity;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

/**
 * It contains methods that map between the different objects that represent a genre
 */
public class GenreMappers {

    public static GenreResponseDTO mapToGenreResponseDTO(GenreEntity entity) {
        if (entity == null) return null;
        GenreResponseDTO response = new GenreResponseDTO();
        BeanUtils.copyProperties(entity, response);
        response.setBooks(entity.getBooks().stream().map(BookEntity::getId).collect(Collectors.toList()));
        return response;
    }

    public static GenreEntity mapToGenreEntity(GenreRequestDTO genreRequestDTO) {
        if (genreRequestDTO == null) return null;
        GenreEntity entity = new GenreEntity();
        BeanUtils.copyProperties(genreRequestDTO, entity);
        return entity;
    }
}
