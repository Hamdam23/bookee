package hamdam.bookee.APIs.genre.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * It's a DTO class that contains the name and description of a genre, and a list of books that belong to that genre
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreRequestDTO {
    @NotBlank(message = "name can not be blank!")
    private String name;

    @Size(max = 200, message = "description size is too long!")
    private String description;

    private List<Long> books;
}
