package hamdam.bookee.APIs.book.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.genre.helpers.GenreResponseDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * It's a DTO that represents response for a book
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
    private String tagline;
    private String description;
    @JsonProperty("authors")
    private List<AppUserResponseDTO> authors = new ArrayList<>();
    private Double rating;
    private List<GenreResponseDTO> genres = new ArrayList<>();
}