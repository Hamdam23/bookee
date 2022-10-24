package hamdam.bookee.APIs.genre.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.GenreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String name;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> books;

    public GenreResponseDTO(GenreEntity entity) {
        BeanUtils.copyProperties(entity, this);
        this.books = entity.getBooks().stream().map(BookEntity::getId).collect(Collectors.toList());
    }
}
