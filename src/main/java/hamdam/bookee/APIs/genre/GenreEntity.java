package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "genres")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name="genre_book",
            joinColumns=@JoinColumn(name="genre_id"),
            inverseJoinColumns=@JoinColumn(name="book_id")
    )
    private List<BookEntity> books = new ArrayList<>();
}
