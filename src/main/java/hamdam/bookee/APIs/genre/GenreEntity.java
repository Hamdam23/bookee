package hamdam.bookee.APIs.genre;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hamdam.bookee.APIs.book.BookEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "genres")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    // TODO: 9/2/22 implement this:
    //TODO when genre deleted corresponding book's genre should be set to "null"
    @ManyToMany(mappedBy = "genres")
    // TODO: 9/2/22 remove @JsonIgnore and user @JsonIgnoreProperties in Book
    @JsonIgnore
    private List<BookEntity> books = new ArrayList<>();

    public GenreEntity(GenreDTO genreDTO) {
        // TODO: 9/2/22 user BeanUtils.copyProperties()
        BeanUtils.copyProperties(genreDTO, this);
//        genreDTO.getBooks().forEach(
//                bookId -> this.books.add(new BookEntity(bookId))
//        );
    }

    public GenreEntity(Long id) {
        this.id = id;
    }
}
