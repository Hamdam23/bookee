package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.genre.GenreEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String tagline;

    private String description;

    private String author;

    private Integer rating;

    @ManyToMany(mappedBy = "books")
    private List<GenreEntity> genres = new ArrayList<>();

    public BookEntity(BookDTO bookDTO) {
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

    public BookEntity() {
    }
}
