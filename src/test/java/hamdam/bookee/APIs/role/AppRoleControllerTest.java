package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.helpers.UserMappers;
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
import java.util.stream.Collectors;

import static hamdam.bookee.APIs.role.Permissions.GET_GENRE;
import static hamdam.bookee.APIs.role.Permissions.GET_USER;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static hamdam.bookee.tools.constants.Endpoints.API_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("name").permissions(Set.of(MONITOR_ROLE)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));
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
        assertThat(roleRepository.existsByRoleName(request.getRoleName())).isTrue();
        assertThat(roleRepository.existsById(response.getId())).isTrue();
    }

    @Test
    void getAllRoles_shouldGetAllRoles() throws Exception {
        //given
        List<AppRoleEntity> roleList = List.of(
                AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE)).build(),
                AppRoleEntity.builder().roleName("dirk").permissions(Set.of(GET_USER)).build(),
                AppRoleEntity.builder().roleName("test").permissions(Set.of(GET_USER)).build(),
                AppRoleEntity.builder().roleName("joke").permissions(Set.of(GET_USER)).build()
        );
        roleRepository.saveAll(roleList);
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", roleList.get(0)));

        roleRepository.saveAll(roleList);

        //when
        ResultActions perform = mockMvc.perform(get(API_ROLE)
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        PagedResponse<AppRoleResponseDTO> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(response.getContent())
                .extracting(AppRoleResponseDTO::getId)
                .containsExactlyInAnyOrderElementsOf(
                        roleList.stream().map(AppRoleEntity::getId).collect(Collectors.toList())
                );
        assertThat(response.getTotalElements()).isEqualTo(roleList.size());

    }

    @Test
    void deleteRoleById_shouldDeleteRole() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_ROLE)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));
        AppRoleEntity existingRole = roleRepository.save(AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE)).build());

        //when
        ResultActions perform = mockMvc.perform(delete(API_ROLE + "/" + existingRole.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk()).andDo(print());
        assertThat(roleRepository.existsById(existingRole.getId())).isFalse();
    }

}