package hamdam.bookee.APIs.genre.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.GenreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
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

    public GenreResponseDTO(GenreRequestDTO genreRequestDTO) {
        BeanUtils.copyProperties(genreRequestDTO, this);
    }

    public GenreResponseDTO(GenreEntity entity) {
        BeanUtils.copyProperties(entity, this);
        this.books = entity.getBooks().stream().map(BookEntity::getId).collect(Collectors.toList());
    }

    public GenreResponseDTO(String name, String description, List<Long> books) {
        this.name = name;
        this.description = description;
        this.books = books;
    }
}
