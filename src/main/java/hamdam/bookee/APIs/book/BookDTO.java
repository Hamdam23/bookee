package hamdam.bookee.APIs.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String name;
    private String tagline;
    private String description;
    private String author;
    private Integer rating;
    private List<Long> genres;

    public BookDTO(Long id, String name){
        this.id = id;
        this.name = name;
    }
}