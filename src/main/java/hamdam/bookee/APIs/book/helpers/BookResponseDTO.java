package hamdam.bookee.APIs.book.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @JsonProperty("author_ids")
    private List<Long> authors = new ArrayList<>();
    private Double rating;
    private List<Long> genres = new ArrayList<>();

    public BookResponseDTO(BookEntity entity) {
        BeanUtils.copyProperties(entity, this);
        this.authors = entity.getAuthors().stream().map(AppUserEntity::getId).collect(Collectors.toList());
        this.genres = entity.getGenres().stream().map(GenreEntity::getId).collect(Collectors.toList());
    }
}