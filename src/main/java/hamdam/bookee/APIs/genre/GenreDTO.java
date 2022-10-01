package hamdam.bookee.APIs.genre;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GenreDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @NotBlank(message = "name can not be blank!")
    private String name;

    @Size(max = 200, message = "description size is too long!")
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> books;

    public GenreDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public GenreDTO(GenreEntity entity){
        BeanUtils.copyProperties(entity, this);
    }
}
