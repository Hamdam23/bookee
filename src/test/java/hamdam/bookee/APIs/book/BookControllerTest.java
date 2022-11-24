package hamdam.bookee.APIs.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.book.helpers.BookRequestDTO;
import hamdam.bookee.APIs.book.helpers.BookResponseDTO;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.genre.GenreRepository;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.paging.PagedResponse;
import hamdam.bookee.tools.utils.TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.*;
import static hamdam.bookee.tools.constants.Endpoints.API_BOOK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenProvider tokenProvider;

    @AfterEach
    void setUp() {
        bookRepository.deleteAll();
        genreRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void addBook_shouldAddBook() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role", Set.of(CREATE_BOOK)));
        AppUserEntity user = userRepository.save(new AppUserEntity("name", "username", "pass", role));
        GenreEntity genre = genreRepository.save(new GenreEntity("name", "desc"));
        BookRequestDTO request = new BookRequestDTO(
                "name",
                "tagline",
                "desc",
                List.of(user.getId()),
                10.0,
                List.of(genre.getId())
        );

        //when
        ResultActions perform = mockMvc.perform(post(API_BOOK)
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        BookResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), BookResponseDTO.class);
        assertThat(response.getName()).isEqualTo(response.getName());
        assertThat(response.getAuthors().get(0)).isEqualTo(user.getId());
    }

    @Test
    void getAllBooks_shouldGetAllBooks() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role", Set.of(GET_BOOK)));
        AppUserEntity user = userRepository.save(new AppUserEntity("name", "username", "pass", role));
        GenreEntity genre = genreRepository.save(new GenreEntity("name", "desc"));

        List<BookEntity> books = List.of(
                bookRepository.save(
                        new BookEntity("hobbit", "h-tag", "h-desc", List.of(user), 10.0, List.of(genre))),
                bookRepository.save(
                        new BookEntity("accountant", "a-tag", "a-desc", List.of(user), 9.0, List.of(genre))),
                bookRepository.save(
                        new BookEntity("harry potter", "hp-tag", "hp-desc", List.of(user), 8.0, List.of(genre)))
        );

        //when
        ResultActions perform = mockMvc.perform(get(API_BOOK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        PagedResponse<BookResponseDTO> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        perform.andExpect(status().isOk());
        assertThat(response.getTotalElements()).isEqualTo(books.size());
    }

    @Test
    void getAllBooks_shouldBookById() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role", Set.of(GET_BOOK)));
        AppUserEntity user = userRepository.save(new AppUserEntity("name", "username", "pass", role));
        GenreEntity genre = genreRepository.save(new GenreEntity("name", "desc"));

        BookEntity book = bookRepository.save(new BookEntity("hobbit", "h-tag", "h-desc", List.of(user), 10.0, List.of(genre)));

        //when
        ResultActions perform = mockMvc.perform(get(API_BOOK + "/" + book.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        BookResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), BookResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(response.getName()).isEqualTo(book.getName());
        assertThat(response.getRating()).isEqualTo(book.getRating());
        assertThat(response.getTagline()).isEqualTo(book.getTagline());
        assertThat(response.getAuthors().get(0)).isEqualTo(book.getAuthors().get(0).getId());
        assertThat(response.getGenres().get(0)).isEqualTo(book.getGenres().get(0).getId());
    }

    @Test
    void updateBook_shouldUpdateBook() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role", Set.of(UPDATE_BOOK)));
        AppUserEntity user = userRepository.save(new AppUserEntity("name", "username", "pass", role));
        GenreEntity genre = genreRepository.save(new GenreEntity("name", "desc"));

        BookEntity book = bookRepository.save(new BookEntity("hobbit", "h-tag", "h-desc", List.of(user), 10.0, List.of(genre)));

        AppUserEntity author = userRepository.save(new AppUserEntity("jack", "black", "good-pass", role));
        BookRequestDTO request = new BookRequestDTO(
                "name",
                "tagline",
                "desc",
                List.of(author.getId()),
                1.0,
                List.of(genre.getId())
        );

        //when
        ResultActions perform = mockMvc.perform(patch(API_BOOK + "/" + book.getId())
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        BookResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), BookResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getRating()).isEqualTo(request.getRating());
        assertThat(response.getTagline()).isEqualTo(request.getTagline());
        assertThat(response.getAuthors().get(0)).isEqualTo(request.getAuthors().get(0));
        assertThat(response.getGenres().get(0)).isEqualTo(request.getGenres().get(0));
    }

    @Test
    void deleteBook_shouldDeleteBook() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role", Set.of(DELETE_BOOK)));
        AppUserEntity user = userRepository.save(new AppUserEntity("name", "username", "pass", role));
        GenreEntity genre = genreRepository.save(new GenreEntity("name", "desc"));
        BookEntity book = bookRepository.save(new BookEntity("hobbit", "h-tag", "h-desc", List.of(user), 10.0, List.of(genre)));

        //when
        ResultActions perform = mockMvc.perform(delete(API_BOOK + "/" + book.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
    }
}