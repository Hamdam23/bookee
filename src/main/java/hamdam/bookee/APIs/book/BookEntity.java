package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
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

    private String name;

    private String tagline;

    private String description;

    // TODO: 9/2/22 make author AppUser, not string
    @ManyToMany
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<AppUserEntity> authors = new ArrayList<>();

    // TODO: 9/2/22 it is better to make rating float/double
    private Double rating;

    @ManyToMany
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres = new ArrayList<>();

    public BookEntity(BookDTO bookDTO) {
        // TODO: 9/2/22 use BeanUtils.copyProperties()
        BeanUtils.copyProperties(bookDTO, this, "id");
        bookDTO.getGenres().forEach(
                genreId -> this.genres.add(new GenreEntity(genreId))
        );
    }
}
