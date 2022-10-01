package hamdam.bookee.APIs.genre;

import hamdam.bookee.APIs.book.BookRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @InjectMocks
    private static GenreServiceImpl underTest;

    @Mock
    private static BookRepository bookRepository;

    @Mock
    private static GenreRepository genreRepository;

    @Test
    void returnValidDataWhen() {
        //given
        GenreDTO dto = new GenreDTO("name", "description");

        //when
        GenreDTO actual = underTest.addGenre(dto);

        //then
        assertThat(actual.getName()).isEqualTo(dto.getName());
        assertThat(actual.getDescription()).isEqualTo(dto.getDescription());
    }

}