package hamdam.bookee.genre;

import hamdam.bookee.book.BookEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "genres")
public class GenreEntity {

    @Id
    private Long id;

    private String name;

    private String description;

    @ManyToMany
    private List<BookEntity> books;
}
