package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.book.helpers.BookRequestDTO;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "books")
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
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<AppUserEntity> authors = new ArrayList<>();

    @DecimalMax("10.0")
    @DecimalMin("0.0")
    private Double rating;

    @NotEmpty(message = "genres can not be empty!")
    @ManyToMany
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres = new ArrayList<>();

    public BookEntity(BookRequestDTO bookRequestDTO) {
        BeanUtils.copyProperties(bookRequestDTO, this, "id");
    }

    public BookEntity(String name, String tagline, String description,
                      List<AppUserEntity> authors, Double rating, List<GenreEntity> genres) {
        this.name = name;
        this.tagline = tagline;
        this.description = description;
        this.authors = authors;
        this.rating = rating;
        this.genres = genres;
    }
}
