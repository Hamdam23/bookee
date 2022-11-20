package hamdam.bookee.APIs.role_request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role_request.helpers.ReviewRequestDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.utils.TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
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
import static hamdam.bookee.APIs.role_request.State.*;
import static hamdam.bookee.tools.constants.Endpoints.API_ROLE_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestRepository roleRequestRepository;

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
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(CREATE_ROLE_REQUEST)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity existingRole = roleRepository.save(new AppRoleEntity("powerful", Set.of(MONITOR_ROLE)));
        RoleRequestDTO request = new RoleRequestDTO(existingRole.getId());

        //when
        ResultActions perform = mockMvc.perform(post(API_ROLE_REQUEST)
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        RoleRequestResponse response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), RoleRequestResponse.class);
        perform.andExpect(status().isOk());
        assertThat(response.getState()).isEqualTo(IN_PROGRESS);
    }

    @Test
    void getAllRoleRequests_shouldGetAllRoleRequests() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(MONITOR_ROLE_REQUEST)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(new AppRoleEntity("user-role", Set.of(GET_ROLE_REQUEST)));
        AppUserEntity phil = userRepository.save(new AppUserEntity("phil", "philly", "pass", userRole));
        AppUserEntity bill = userRepository.save(new AppUserEntity("bill", "billy", "pass", userRole));
        AppUserEntity sam = userRepository.save(new AppUserEntity("sam", "sammy", "pass", userRole));
        roleRequestRepository.save(new RequestEntity(phil, role, ACCEPTED));
        roleRequestRepository.save(new RequestEntity(bill, role, IN_PROGRESS));
        roleRequestRepository.save(new RequestEntity(sam, role, DECLINED));

        //when
        ResultActions perform = mockMvc.perform(get(API_ROLE_REQUEST)
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        List<RoleRequestResponse> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        perform.andExpect(status().isOk());
        assertThat(response.size()).isEqualTo(3);
    }

    @Test
    @Disabled
    void getAllRoleRequests_shouldGetAllAcceptedRoleRequests() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(MONITOR_ROLE_REQUEST)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(new AppRoleEntity("user-role", Set.of(GET_ROLE_REQUEST)));
        AppUserEntity phil = userRepository.save(new AppUserEntity("phil", "philly", "pass", userRole));
        AppUserEntity bill = userRepository.save(new AppUserEntity("bill", "billy", "pass", userRole));
        AppUserEntity sam = userRepository.save(new AppUserEntity("sam", "sammy", "pass", userRole));
        roleRequestRepository.save(new RequestEntity(phil, role, ACCEPTED));
        roleRequestRepository.save(new RequestEntity(bill, role, ACCEPTED));
        roleRequestRepository.save(new RequestEntity(sam, role, DECLINED));

        //when
        ResultActions perform = mockMvc.perform(get(API_ROLE_REQUEST)
                .contentType(APPLICATION_JSON)
                .param("status", ACCEPTED.name())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        List<RoleRequestResponse> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        perform.andExpect(status().isOk());
        assertThat(response.size()).isEqualTo(2);
    }

    @Test
    void reviewRequest_shouldReviewRoleRequest() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(MONITOR_ROLE_REQUEST)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(new AppRoleEntity("user-role", Set.of(GET_ROLE_REQUEST)));
        AppUserEntity phil = userRepository.save(new AppUserEntity("phil", "philly", "pass", userRole));
        RequestEntity existingRequest = roleRequestRepository.save(new RequestEntity(phil, role, IN_PROGRESS));

        ReviewRequestDTO request = new ReviewRequestDTO(DECLINED, "very bad");

        //when
        ResultActions perform = mockMvc.perform(put(API_ROLE_REQUEST + "/" + existingRequest.getId())
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        RoleRequestResponse response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), RoleRequestResponse.class);
        perform.andExpect(status().isOk());
        assertThat(response.getId()).isEqualTo(existingRequest.getId());
        assertThat(response.getState()).isEqualTo(DECLINED);
    }

    @Test
    void reviewRequest_shouldDeleteRoleRequest() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(new AppRoleEntity("role-name", Set.of(MONITOR_ROLE_REQUEST)));
        AppUserEntity user = userRepository.save(new AppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(new AppRoleEntity("user-role", Set.of(GET_ROLE_REQUEST)));
        AppUserEntity phil = userRepository.save(new AppUserEntity("phil", "philly", "pass", userRole));
        RequestEntity existingRequest = roleRequestRepository.save(new RequestEntity(phil, role, IN_PROGRESS));

        //when
        ResultActions perform = mockMvc.perform(delete(API_ROLE_REQUEST + "/" + existingRequest.getId())
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        assertThat(roleRequestRepository.existsById(existingRequest.getId())).isFalse();
    }

}