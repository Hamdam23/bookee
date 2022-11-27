package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.helpers.GenreRequestDTO;
import hamdam.bookee.APIs.genre.helpers.GenreResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GenreMappers {

    public GenreRequestDTO mapToGenreRequestDTO(String name, String description) {
        GenreRequestDTO genreRequest = new GenreRequestDTO();
        genreRequest.setName(name);
        genreRequest.setDescription(description);
        return genreRequest;
    }

    public GenreResponseDTO mapToGenreResponseDTO(GenreEntity entity) {
        GenreResponseDTO response = new GenreResponseDTO();
        BeanUtils.copyProperties(entity, response);
        response.setBooks(entity.getBooks().stream().map(BookEntity::getId).collect(Collectors.toList()));
        return response;
    }

    public GenreEntity mapToGenreEntity(GenreRequestDTO genreRequestDTO) {
        GenreEntity entity = new GenreEntity();
        BeanUtils.copyProperties(genreRequestDTO, entity);
        return entity;
    }

    public GenreEntity mapToGenreEntity(String name, String description) {
        GenreEntity entity = new GenreEntity();
        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }
}
