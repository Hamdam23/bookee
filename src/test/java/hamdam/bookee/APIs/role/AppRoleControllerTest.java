package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
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
import static hamdam.bookee.tools.constants.Endpoints.API_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AppRoleControllerTest {

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
    void postRole_shouldPostRole() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(MONITOR_ROLE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));
        AppRoleRequestDTO request = new AppRoleRequestDTO("sniper", false, Set.of(GET_USER, GET_GENRE));

        //when
        ResultActions perform = mockMvc.perform(post(API_ROLE)
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        AppRoleResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), AppRoleResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(response.getRoleName()).isEqualTo(request.getRoleName());
        assertThat(response.getPermissions()).isEqualTo(request.getPermissions());
    }

    @Test
    void getAllRoles_shouldGetAllRoles() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(MONITOR_ROLE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));

        List<AppRoleEntity> roleList = List.of(
                new AppRoleEntity("dirk", Set.of(GET_USER)),
                new AppRoleEntity("niko", Set.of(GET_USER)),
                new AppRoleEntity("dev1ce", Set.of(GET_USER))
        );
        roleRepository.saveAll(roleList);

        //when
        String content = Objects.requireNonNull(objectMapper.writeValueAsString(roleList));
        ResultActions perform = mockMvc.perform(get(API_ROLE)
                .contentType(APPLICATION_JSON)
                .content(content)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        PagedResponse<AppRoleResponseDTO> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(response.getContent().get(0).getId()).isEqualTo(roleList.get(2).getId());
        assertThat(response.getContent().get(1).getId()).isEqualTo(roleList.get(1).getId());
        assertThat(response.getContent().get(2).getId()).isEqualTo(roleList.get(0).getId());
        assertThat(response.getTotalElements()).isEqualTo(roleList.size() + 1);
    }

    @Test
    void deleteRoleById_shouldDeleteRole() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(MONITOR_ROLE)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));
        AppRoleEntity existingRole = roleRepository.save(new AppRoleEntity("lurker", Set.of(MONITOR_ROLE)));

        //when
        ResultActions perform = mockMvc.perform(delete(API_ROLE + "/" + existingRole.getId())
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk()).andDo(print());
    }

}