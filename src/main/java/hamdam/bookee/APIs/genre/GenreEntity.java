package hamdam.bookee.APIs.genre;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hamdam.bookee.APIs.book.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hamdam.bookee.tools.constants.Patterns.TIMESTAMP_PATTERN;
import static hamdam.bookee.tools.constants.TableNames.TABLE_NAME_GENRE;

/**
 * GenreEntity is a class that represents a genre of a book.
 * It has a name and a description. It also has a list of books that belong to this genre.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = TABLE_NAME_GENRE)
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

    @JsonFormat(pattern = TIMESTAMP_PATTERN)
    @UpdateTimestamp
    private LocalDateTime timeStamp;

}
