package hamdam.bookee.APIs.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookDTO {
    private String name;
    private String tagline;
    private String description;
    private String author;
    private Integer rating;
    private List<Long> genres;
}