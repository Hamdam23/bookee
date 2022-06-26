package hamdam.bookee.APIs.genre;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private List<BookEntity> books = new ArrayList<>();

    public GenreEntity(GenreDTO genreDTO) {
        this.name = genreDTO.getName();
        this.description = genreDTO.getDescription();
        genreDTO.getBooks().forEach(
                bookId -> this.books.add(new BookEntity(bookId))
        );;
    }

    public GenreEntity(Long id) {
        this.id = id;
    }

    public GenreEntity() {
    }
}
