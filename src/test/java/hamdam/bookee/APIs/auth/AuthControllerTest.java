package hamdam.bookee.APIs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.utils.TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static hamdam.bookee.tools.constants.Endpoints.API_REGISTER;
import static hamdam.bookee.tools.constants.Endpoints.API_TOKEN_REFRESH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void register_shouldRegisterUser() throws Exception {
        //given
        String username = "blood";
        RegistrationRequest request = new RegistrationRequest("blood-seeker", username, "pass");
        roleRepository.save(new AppRoleEntity("role-name", true, LocalDateTime.now()));

        //when
        ResultActions perform = mockMvc.perform(post(API_REGISTER)
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
        );

        //then
        TokensResponse response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), TokensResponse.class);
        perform.andExpect(status().isOk());
        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getAccessTokenExpiry()).isNotBlank();
        assertThat(response.getRefreshToken()).isNotBlank();
        assertThat(response.getRefreshTokenExpiry()).isNotBlank();
        assertThat(userRepository.existsByUsername(username)).isTrue();
    }

    @Test
    void refreshToken_shouldRefreshToken() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(MONITOR_ROLE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));

        //when
        ResultActions perform = mockMvc.perform(get(API_TOKEN_REFRESH)
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), false))
        );

        //then
        TokensResponse response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), TokensResponse.class);
        perform.andExpect(status().isOk());
        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getAccessTokenExpiry()).isNotBlank();
    }

}