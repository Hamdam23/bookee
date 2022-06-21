package hamdam.bookee.book;

import hamdam.bookee.genre.GenreEntity;
import lombok.Data;

import javax.persistence.*;
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

    private int rating;

    @ManyToMany(mappedBy = "books")
    private List<GenreEntity> genres;

}
