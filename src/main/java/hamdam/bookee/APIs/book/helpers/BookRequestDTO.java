package hamdam.bookee.APIs.book.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDTO {

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

    public BookRequestDTO(String name, List<Long> authors) {
        this.name = name;
        this.authors = authors;
    }

    public BookRequestDTO(String name, Double rating, List<Long> genres) {
        this.name = name;
        this.rating = rating;
        this.genres = genres;
    }
}