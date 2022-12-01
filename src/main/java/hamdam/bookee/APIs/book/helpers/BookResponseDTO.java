package hamdam.bookee.APIs.book.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.helpers.GenreResponseDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * It's a DTO that represents response for a book
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
    private String tagline;
    private String description;
    @JsonProperty("authors")
    private List<AppUserResponseDTO> authors = new ArrayList<>();
    private Double rating;
    private List<GenreResponseDTO> genres = new ArrayList<>();

    public BookResponseDTO(BookEntity entity) {
        BeanUtils.copyProperties(entity, this);
        this.authors = entity.getAuthors().stream()
                .map(AppUserResponseDTO::new)
                .collect(Collectors.toList());
        this.genres = entity.getGenres().stream()
                .map(GenreResponseDTO::new)
                .collect(Collectors.toList());
    }
}