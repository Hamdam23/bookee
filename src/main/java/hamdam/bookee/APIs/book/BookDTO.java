package hamdam.bookee.APIs.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
    private String tagline;
    private String description;
    private List<Long> authors = new ArrayList<>();
    private Double rating;
    private List<Long> genres = new ArrayList<>();

    public BookDTO(BookEntity entity) {
        BeanUtils.copyProperties(entity, this);

        List<Long> authorIDs = new ArrayList<>();
        entity.getAuthors().forEach(author ->
                authorIDs.add(author.getId()));
        this.authors = authorIDs;
    }
}