package hamdam.bookee.APIs.genre;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hamdam.bookee.APIs.book.BookEntity;
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
@JsonIgnoreProperties(value = { "books" })
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "genre name can not be blank!")
    private String name;

    @Size(max = 200, message = "description size is too long!")
    private String description;

    // TODO: 9/2/22 implement this:
    //TODO when genre deleted corresponding book's genre should be set to "null"
    @ManyToMany(mappedBy = "genres")
    // TODO: 9/2/22 remove @JsonIgnore and user @JsonIgnoreProperties in Book
//    @JsonIgnore
    private List<BookEntity> books = new ArrayList<>();

    public GenreEntity(GenreDTO genreDTO) {
        // TODO: 9/2/22 user BeanUtils.copyProperties()
        BeanUtils.copyProperties(genreDTO, this);
    }

    public GenreEntity(Long id) {
        this.id = id;
    }
}
