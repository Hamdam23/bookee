package hamdam.bookee.APIs.role_request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role_request.helpers.ReviewRequest;
import hamdam.bookee.APIs.role_request.helpers.RoleIdRoleRequest;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestResponseDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.helpers.UserMappers;
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

import static hamdam.bookee.APIs.role.Permissions.CREATE_ROLE_REQUEST;
import static hamdam.bookee.APIs.role.Permissions.GET_ROLE_REQUEST;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE_REQUEST;
import static hamdam.bookee.APIs.role_request.State.ACCEPTED;
import static hamdam.bookee.APIs.role_request.State.DECLINED;
import static hamdam.bookee.APIs.role_request.State.IN_PROGRESS;
import static hamdam.bookee.tools.constants.Endpoints.API_ROLE_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RoleRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRequestRepository roleRequestRepository;

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
        roleRequestRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void sendRoleRequest_shouldSendRoleRequest() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(CREATE_ROLE_REQUEST)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity existingRole = roleRepository.save(AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE)).build());
        RoleIdRoleRequest request = new RoleIdRoleRequest(existingRole.getId());

        //when
        ResultActions perform = mockMvc.perform(post(API_ROLE_REQUEST)
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        RoleRequestResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), RoleRequestResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(roleRequestRepository.existsById(response.getId())).isTrue();
        assertThat(response.getState()).isEqualTo(IN_PROGRESS);
    }

    @Test
    void getAllRoleRequests_shouldGetAllRoleRequests() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_ROLE_REQUEST)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_ROLE_REQUEST)).build());
        AppUserEntity phil = userRepository.save(UserMappers.mapToAppUserEntity("phil", "philly", "pass", userRole));
        AppUserEntity bill = userRepository.save(UserMappers.mapToAppUserEntity("bill", "billy", "pass", userRole));
        AppUserEntity sam = userRepository.save(UserMappers.mapToAppUserEntity("sam", "sammy", "pass", userRole));
        List<RoleRequestEntity> requestEntities = List.of(
                RoleRequestEntity.builder().user(phil).requestedRole(role).state(ACCEPTED).build(),
                RoleRequestEntity.builder().user(bill).requestedRole(role).state(IN_PROGRESS).build(),
                RoleRequestEntity.builder().user(sam).requestedRole(role).state(DECLINED).build()
        );
        roleRequestRepository.saveAll(requestEntities);

        //when
        ResultActions perform = mockMvc.perform(get(API_ROLE_REQUEST)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        List<RoleRequestResponseDTO> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        perform.andExpect(status().isOk());
        assertThat(response)
                .extracting(RoleRequestResponseDTO::getId)
                .containsExactlyInAnyOrderElementsOf(
                        requestEntities.stream().map(RoleRequestEntity::getId).collect(Collectors.toList())
                );
        assertThat(response.size()).isEqualTo(requestEntities.size());
    }

    @Test
    void getAllRoleRequests_shouldGetAllAcceptedRoleRequests() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE_REQUEST)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_ROLE_REQUEST)).build());
        AppUserEntity phil = userRepository.save(UserMappers.mapToAppUserEntity("phil", "philly", "pass", userRole));
        AppUserEntity bill = userRepository.save(UserMappers.mapToAppUserEntity("bill", "billy", "pass", userRole));
        AppUserEntity sam = userRepository.save(UserMappers.mapToAppUserEntity("sam", "sammy", "pass", userRole));
        List<RoleRequestEntity> requestEntities = List.of(
                RoleRequestEntity.builder().user(phil).requestedRole(role).state(ACCEPTED).build(),
                RoleRequestEntity.builder().user(bill).requestedRole(role).state(ACCEPTED).build(),
                RoleRequestEntity.builder().user(sam).requestedRole(role).state(DECLINED).build()
        );
        roleRequestRepository.saveAll(requestEntities);

        //when
        ResultActions perform = mockMvc.perform(get(API_ROLE_REQUEST)
                .param("state", ACCEPTED.name())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        List<RoleRequestResponseDTO> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        perform.andExpect(status().isOk());
        assertThat(response)
                .extracting(RoleRequestResponseDTO::getId)
                .containsExactlyInAnyOrderElementsOf(
                        requestEntities.stream()
                                .filter(state -> (state.getState() == ACCEPTED))
                                .map(RoleRequestEntity::getId).collect(Collectors.toList())
                );
        assertThat(response.size()).isEqualTo(requestEntities.stream().filter(state -> (state.getState() == ACCEPTED)).count());
    }

    @Test
    void reviewRequest_shouldReviewRoleRequest() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_ROLE_REQUEST)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_ROLE_REQUEST)).build());
        AppUserEntity phil = userRepository.save(UserMappers.mapToAppUserEntity("phil", "philly", "pass", userRole));
        RoleRequestEntity existingRequest = roleRequestRepository.save(RoleRequestEntity.builder().user(phil).requestedRole(role).state(IN_PROGRESS).build());

        ReviewRequest request = new ReviewRequest(DECLINED, "very bad");

        //when
        ResultActions perform = mockMvc.perform(put(API_ROLE_REQUEST + "/" + existingRequest.getId())
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        RoleRequestResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), RoleRequestResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(response.getId()).isEqualTo(existingRequest.getId());
        assertThat(response.getState()).isEqualTo(request.getState());
    }

    @Test
    void reviewRequest_shouldDeleteRoleRequest() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_ROLE_REQUEST)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_ROLE_REQUEST)).build());
        AppUserEntity phil = userRepository.save(UserMappers.mapToAppUserEntity("phil", "philly", "pass", userRole));
        RoleRequestEntity existingRequest = roleRequestRepository.save(RoleRequestEntity.builder().user(phil).requestedRole(role).state(IN_PROGRESS).build());

        //when
        ResultActions perform = mockMvc.perform(delete(API_ROLE_REQUEST + "/" + existingRequest.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        assertThat(roleRequestRepository.existsById(existingRequest.getId())).isFalse();
    }

}