package hamdam.bookee.APIs.genre;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.helpers.GenreRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "genres")
@JsonIgnoreProperties(value = {"books"})
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "genre name can not be blank!")
    private String name;

    @Size(max = 200, message = "description size is too long!")
    private String description;

    //TODO when genre deleted corresponding book's genre should be set to "null"
    @ManyToMany(mappedBy = "genres")
    private List<BookEntity> books = new ArrayList<>();

    public GenreEntity(GenreRequestDTO genreRequestDTO) {
        BeanUtils.copyProperties(genreRequestDTO, this);
    }

    public GenreEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
