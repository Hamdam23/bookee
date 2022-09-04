package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.genre.GenreEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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
    private String author;

    // TODO: 9/2/22 it is better to make rating float/double
    private Integer rating;

    @ManyToMany
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres = new ArrayList<>();

    public BookEntity(BookDTO bookDTO) {
        // TODO: 9/2/22 use BeanUtils.copyProperties()
        this.name = bookDTO.getName();
        this.tagline = bookDTO.getTagline();
        this.description = bookDTO.getDescription();
        this.author = bookDTO.getAuthor();
        this.rating = bookDTO.getRating();
        bookDTO.getGenres().forEach(
                genreId -> this.genres.add(new GenreEntity(genreId))
        );
    }

    public BookEntity(Long id) {
        this.id = id;
    }
}
