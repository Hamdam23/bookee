package hamdam.bookee.APIs.genre.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * It's a DTO that represents response on Genre API
 */
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
}
