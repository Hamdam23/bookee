package hamdam.bookee.APIs.genre.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreRequestDTO {
    @NotBlank(message = "name can not be blank!")
    private String name;

    @Size(max = 200, message = "description size is too long!")
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> books;

    public GenreRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
