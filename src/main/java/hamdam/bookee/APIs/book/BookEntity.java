package hamdam.bookee.APIs.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hamdam.bookee.tools.constants.Patterns.TIMESTAMP_PATTERN;
import static hamdam.bookee.tools.constants.TableNames.*;

/**
 * It's a book entity with a name, tagline, description, authors, rating, and genres
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = TABLE_NAME_BOOK)
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name can not be blank!")
    private String name;

    @Size(max = 100, message = "tagline size is too long!")
    private String tagline;

    @Size(max = 200, message = "description size is too long!")
    private String description;

    @NotEmpty(message = "authors can not be empty!")
    @ManyToMany
    @JoinTable(name = TABLE_NAME_BOOK_AUTHOR,
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<AppUserEntity> authors = new ArrayList<>();

    @DecimalMax("10.0")
    @DecimalMin("0.0")
    private Double rating;

    @NotEmpty(message = "genres can not be empty!")
    @ManyToMany
    @JoinTable(name = TABLE_NAME_BOOK_GENRE,
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres = new ArrayList<>();

    @JsonFormat(pattern = TIMESTAMP_PATTERN)
    @UpdateTimestamp
    private LocalDateTime timeStamp;
}
