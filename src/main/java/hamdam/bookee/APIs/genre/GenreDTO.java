package hamdam.bookee.APIs.genre;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GenreDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> books;

    public GenreDTO(GenreEntity entity){
        BeanUtils.copyProperties(entity, this);
    }
}
