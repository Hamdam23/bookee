package hamdam.bookee.APIs.book.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.book.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @NotBlank(message = "name can not be blank!")
    private String name;

    @Size(max = 30, message = "tagline size is too long!")
    private String tagline;

    @Size(max = 200, message = "description size is too long!")
    private String description;

    @JsonProperty("author_ids")
    @NotEmpty(message = "authors can not be empty!")
    private List<Long> authors = new ArrayList<>();

    @DecimalMax("10.0")
    @DecimalMin("0.0")
    private Double rating;

    @NotEmpty(message = "genres can not be empty!")
    private List<Long> genres = new ArrayList<>();

    public BookDTO(BookEntity entity) {
        BeanUtils.copyProperties(entity, this);

        List<Long> authorIDs = new ArrayList<>();
        entity.getAuthors().forEach(author ->
                authorIDs.add(author.getId()));
        this.authors = authorIDs;
    }
}