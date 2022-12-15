package hamdam.bookee.APIs.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.image.helpers.UserImageDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.SetRoleUserRequest;
import hamdam.bookee.APIs.user.helpers.UpdatePasswordRequest;
import hamdam.bookee.APIs.user.helpers.UserMappers;
import hamdam.bookee.tools.paging.PagedResponse;
import hamdam.bookee.tools.utils.TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static hamdam.bookee.APIs.role.Permissions.GET_USER;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static hamdam.bookee.tools.constants.Endpoints.API_SET_ROLE_USER;
import static hamdam.bookee.tools.constants.Endpoints.API_USER;
import static hamdam.bookee.tools.constants.Endpoints.SET_IMAGE_TO_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    void getAllUsers_shouldGetAllUsers() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_USER)).build());
        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_USER)).build());

        List<AppUserEntity> users = List.of(
                UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role),
                UserMappers.mapToAppUserEntity("phil", "philly", "pass", userRole),
                UserMappers.mapToAppUserEntity("blood", "bloody", "pass", userRole),
                UserMappers.mapToAppUserEntity("shaker", "shaky", "pass", userRole)
        );
        userRepository.saveAll(users);

        //when
        ResultActions perform = mockMvc.perform(get(API_USER)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(users.get(0).getUsername(), users.get(0).getRole(), true))
        );

        //then
        PagedResponse<AppUserResponseDTO> response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        perform.andExpect(status().isOk());
        assertThat(response.getContent())
                .extracting(AppUserResponseDTO::getId)
                .containsExactlyInAnyOrderElementsOf(
                        users.stream().map(AppUserEntity::getId).collect(Collectors.toList())
                );
        assertThat(response.getTotalElements()).isEqualTo(users.size());
    }

    @Test
    void getUser_shouldGetUserById() throws Exception {
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_USER)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_USER)).build());
        List<AppUserEntity> users = List.of(
                UserMappers.mapToAppUserEntity("phil", "philly", "pass", userRole),
                UserMappers.mapToAppUserEntity("blood", "bloody", "pass", userRole),
                UserMappers.mapToAppUserEntity("shaker", "shaky", "pass", userRole)
        );
        userRepository.saveAll(users);

        //when
        ResultActions perform = mockMvc.perform(get(API_USER + "/" + users.get(0).getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        AppUserResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), AppUserResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(response.getUsername()).isEqualTo(users.get(0).getUsername());
    }

    @Test
    void updateUser_shouldUpdateUser() throws Exception {
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_USER)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_USER)).build());
        AppUserEntity bill = userRepository.save(UserMappers.mapToAppUserEntity("bill", "billy", "pass", userRole));

        AppUserRequestDTO request = UserMappers.mapToAppUserRequestDTO("jon", "jonny");

        //when
        ResultActions perform = mockMvc.perform(patch(API_USER + "/" + bill.getId())
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        assertThat(userRepository.findById(bill.getId()).get().getUsername()).isEqualTo(request.getUsername());
    }

    @Test
    void updatePassword_shouldUpdatePassword() throws Exception {
        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("role").permissions(Set.of(GET_USER)).build());
        String oldPassword = "12345";
        AppUserEntity bill = userRepository.save(UserMappers.mapToAppUserEntity(
                "bill",
                "billy",
                passwordEncoder.encode(oldPassword),
                userRole)
        );
        String newPassword = "new-pass";
        String confirmNewPassword = "new-pass";
        UpdatePasswordRequest request = new UpdatePasswordRequest(oldPassword, newPassword, confirmNewPassword);

        //when
        ResultActions perform = mockMvc.perform(patch(API_USER + "/change-password/" + bill.getId())
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(bill.getUsername(), bill.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        assertThat(passwordEncoder.matches(newPassword, userRepository.findById(bill.getId()).get().getPassword())).isTrue();
    }

    @Test
    void setRoleToUser_shouldSetRoleToUser() throws Exception {
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_USER)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_USER)).build());
        AppUserEntity bill = userRepository.save(UserMappers.mapToAppUserEntity("bill", "billy", "pass", userRole));

        SetRoleUserRequest request = new SetRoleUserRequest(role.getId());

        //when
        ResultActions perform = mockMvc.perform(patch(API_USER + API_SET_ROLE_USER + "/" + bill.getId())
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        AppUserResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), AppUserResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(response.getRole().getId()).isEqualTo(role.getId());
    }

    @Test
    void setImageToUser_shouldSetImageToUser() throws Exception {
        //given
        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("role").permissions(Set.of(GET_USER)).build());
        AppUserEntity bill = userRepository.save(UserMappers.mapToAppUserEntity("bill", "billy", "pass", userRole));

        ImageEntity image = imageRepository.save(ImageEntity.builder().imageName("name").url("url").build());
        UserImageDTO request = new UserImageDTO(image.getId());

        //when
        ResultActions perform = mockMvc.perform(patch(API_USER + SET_IMAGE_TO_USER + "/" + bill.getId())
                .contentType(APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(bill.getUsername(), bill.getRole(), true))
        );

        //then
        AppUserResponseDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), AppUserResponseDTO.class);
        perform.andExpect(status().isOk());
        assertThat(response.getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    void deleteUser_shouldDeleteUser() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("role-name").permissions(Set.of(MONITOR_USER)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        AppRoleEntity userRole = roleRepository.save(AppRoleEntity.builder().roleName("user-role").permissions(Set.of(GET_USER)).build());
        AppUserEntity bill = userRepository.save(UserMappers.mapToAppUserEntity("bill", "billy", "pass", userRole));

        //when
        ResultActions perform = mockMvc.perform(delete(API_USER + "/" + bill.getId())
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        assertThat(userRepository.existsById(bill.getId())).isFalse();
    }
}