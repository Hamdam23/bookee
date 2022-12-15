package hamdam.bookee.APIs.genre.helpers;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.GenreEntity;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

/**
 * It contains methods that map between the different objects that represent a genre
 */
public class GenreMappers {

    public static GenreRequestDTO mapToGenreRequestDTO(String name, String description) {
        GenreRequestDTO genreRequest = new GenreRequestDTO();
        genreRequest.setName(name);
        genreRequest.setDescription(description);
        return genreRequest;
    }

    public static GenreResponseDTO mapToGenreResponseDTO(GenreEntity entity) {
        if (entity == null) return null;
        GenreResponseDTO response = new GenreResponseDTO();
        BeanUtils.copyProperties(entity, response);
        response.setBooks(entity.getBooks().stream().map(BookEntity::getId).collect(Collectors.toList()));
        return response;
    }

    public static GenreEntity mapToGenreEntity(GenreRequestDTO genreRequestDTO) {
        GenreEntity entity = new GenreEntity();
        BeanUtils.copyProperties(genreRequestDTO, entity);
        return entity;
    }

    public static GenreEntity mapToGenreEntity(String name, String description) {
        GenreEntity entity = new GenreEntity();
        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }
}
