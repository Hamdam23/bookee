package hamdam.bookee.APIs.genre;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.genre.helpers.GenreRequestDTO;
import hamdam.bookee.APIs.genre.helpers.GenreResponseDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.Permissions;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static hamdam.bookee.tools.constants.Endpoints.API_GENRE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void setup() {
        genreRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void addGenre_shouldPostGenre() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(Permissions.CREATE_GENRE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));
        GenreRequestDTO request = new GenreRequestDTO("action", "very good desc");

        //when
        ResultActions perform = mockMvc.perform(post(API_GENRE)
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        GenreResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), GenreResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getDescription()).isEqualTo(request.getDescription());
        // TODO: 11/18/22 i think this assertion is not enough, you should also check if genre is saved in database
    }

    @Test
    void getAllGenres_shouldGetAllGenres() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(Permissions.GET_GENRE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));

        List<GenreEntity> genreList = List.of(
                new GenreEntity("dirk", "desc"),
                new GenreEntity("niko", "good desc"),
                new GenreEntity("dev1ce", "very good desc")
        );
        genreRepository.saveAll(genreList);

        //when
        String content = Objects.requireNonNull(objectMapper.writeValueAsString(genreList));
        ResultActions perform = mockMvc.perform(get(API_GENRE)
                .contentType(APPLICATION_JSON)
                .content(content)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        PagedResponse<GenreResponseDTO> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(response.getContent())
                .extracting(GenreResponseDTO::getId)
                .containsExactlyInAnyOrderElementsOf(
                        genreList.stream().map(GenreEntity::getId).collect(Collectors.toList())
                );
        assertThat(response.getTotalElements()).isEqualTo(genreList.size());
    }

    @Test
    void getGenreByID_shouldGetGenreById() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(Permissions.GET_GENRE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));
        GenreEntity genre = genreRepository.save(new GenreEntity("dirk", "desc"));

        //when
        String content = Objects.requireNonNull(objectMapper.writeValueAsString(genre));
        ResultActions perform = mockMvc.perform(get(API_GENRE + "/{id}", genre.getId())
                .contentType(APPLICATION_JSON)
                .content(content)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        GenreResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), GenreResponseDTO.class);
        assertThat(response.getName()).isEqualTo(genre.getName());
        assertThat(response.getDescription()).isEqualTo(genre.getDescription());
    }

    @Test
    void updateGenre_shouldUpdateGenre() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(Permissions.UPDATE_GENRE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));
        GenreEntity existingGenre = genreRepository.save(new GenreEntity("blood-seeker", "desc"));

        GenreRequestDTO genreRequest = new GenreRequestDTO(
                "dirk",
                "good desc",
                new ArrayList<>()
        );

        //when
        String content = Objects.requireNonNull(objectMapper.writeValueAsString(genreRequest));
        ResultActions perform = mockMvc.perform(patch(API_GENRE + "/" + existingGenre.getId())
                .contentType(APPLICATION_JSON)
                .content(content)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk()).andDo(print());

        GenreEntity updatedGenre = genreRepository.findById(existingGenre.getId()).get();
        assertThat(updatedGenre.getName()).isEqualTo(genreRequest.getName());
        assertThat(updatedGenre.getDescription()).isEqualTo(genreRequest.getDescription());
    }

    @Test
    void deleteGenre_shouldDeleteGenre() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(Permissions.DELETE_GENRE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));
        GenreEntity existingGenre = genreRepository.save(new GenreEntity("dirk", "desc"));

        //when
        ResultActions perform = mockMvc.perform(delete(API_GENRE + "/" + existingGenre.getId())
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk()).andDo(print());
        assertThat(genreRepository.existsById(existingGenre.getId())).isFalse();
    }

}