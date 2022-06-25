package hamdam.bookee.APIs.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GenreDTO {

    private String name;

    private String description;

    private List<Long> books;
}
