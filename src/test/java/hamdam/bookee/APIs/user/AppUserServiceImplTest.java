package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.user.helpers.UserRequestDTO;
import hamdam.bookee.APIs.user.helpers.UserResponseDTO;
import hamdam.bookee.APIs.user.helpers.PasswordUpdateRequest;
import hamdam.bookee.APIs.user.helpers.SetUserImageRequest;
import hamdam.bookee.APIs.user.helpers.SetUseRoleRequest;
import hamdam.bookee.tools.exceptions.DuplicateResourceException;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import hamdam.bookee.tools.exceptions.role.NoDefaultRoleException;
import hamdam.bookee.tools.exceptions.user.PasswordMismatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SecurityTestExecutionListeners
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppRoleRepository appRoleRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserServiceImpl underTest;

    @Test
    void saveUser_throwsExceptionWhenUsernameIsDuplicate() {
        //given
        String username = "niko";
        when(appUserRepository.existsByUsername(username)).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.saveUser(new RegistrationRequest(null, username, null, null)))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining(username);
        verify(appUserRepository).existsByUsername(username);
    }

    @Test
    void saveUser_throwsExceptionWhenImageIdIsInvalid() {
        //given
        String username = "niko";
        Long imageId = 1L;
        when(appUserRepository.existsByUsername(username)).thenReturn(false);
        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.saveUser(new RegistrationRequest(null, username, null, imageId)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(imageId.toString());
        verify(appUserRepository).existsByUsername(username);
        verify(imageRepository).findById(imageId);
    }

    @Test
    void saveUser_throwsExceptionWhenNoDefaultRoleAvailable() {
        //given
        String username = "niko";
        Long imageId = 1L;
        when(appUserRepository.existsByUsername(username)).thenReturn(false);
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(new ImageEntity()));
        when(appRoleRepository.findFirstByIsDefaultIsTrue()).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.saveUser(
                new RegistrationRequest(null, username, "12345", imageId))
        ).isInstanceOf(NoDefaultRoleException.class);
        verify(appUserRepository).existsByUsername(username);
        verify(imageRepository).findById(imageId);
        verify(appRoleRepository).findFirstByIsDefaultIsTrue();
    }

    @Test
    void saveUser_returnValidDataWhenRequestIsValid() {
        //given
        String name = "Nicola";
        String username = "niko";
        String password = "12345";
        Long imageId = 1L;
        AppUserEntity user = AppUserEntity
                .builder()
                .name(name)
                .username(username)
                .password("very_secret_password")
                .build();
        when(appUserRepository.existsByUsername(username)).thenReturn(false);
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(new ImageEntity()));
        when(appRoleRepository.findFirstByIsDefaultIsTrue()).thenReturn(Optional.of(new AppRoleEntity()));
        when(appUserRepository.save(any())).thenReturn(user);

        //when
        underTest.saveUser(new RegistrationRequest(name, username, password, imageId));

        //then
        verify(appUserRepository).existsByUsername(username);
        verify(imageRepository).findById(imageId);
        verify(appRoleRepository).findFirstByIsDefaultIsTrue();
        ArgumentCaptor<AppUserEntity> argumentCaptor = ArgumentCaptor.forClass(AppUserEntity.class);
        verify(appUserRepository).save(argumentCaptor.capture());
        AppUserEntity actual = argumentCaptor.getValue();
        verify(appUserRepository).save(any());

        assertThat(actual.getUsername()).isEqualTo(username);
    }

    @Test
    void getAppUserById_shouldThrowExceptionWhenUserIdIsInvalid() {
        //given
        Long userId = 1L;
        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("id")
                .hasMessageContaining(userId.toString());
    }

    @Test
    void getAppUserById_shouldReturnValidDataWhenUserIdIsValid() {
        //given
        Long userId = 1L;
        AppUserEntity expected = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .password("pass")
                .build();
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(expected));

        //when
        AppUserEntity actual = underTest.getAppUserById(userId);

        //then
        verify(appUserRepository).findById(userId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void loadUserByUsername_throwsExceptionWhenUsernameIsInvalid() {
        //given
        String username = "test";
        when(appUserRepository.findAppUserByUsernameWithPermission(username)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.loadUserByUsername(username))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("username")
                .hasMessageContaining(username);
    }

    @Test
    void loadUserByUsername_shouldReturnValidDataWhenUsernameIsValid() {
        //given
        String username = "test";
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Set.of(MONITOR_ROLE, MONITOR_USER)).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .username("niko")
                .role(role)
                .build();
        user.setPassword("very_good_password");
        when(appUserRepository.findAppUserByUsernameWithPermission(username)).thenReturn(Optional.of(user));

        //when
        UserDetails actual = underTest.loadUserByUsername(username);

        //then
        verify(appUserRepository).findAppUserByUsernameWithPermission(username);
        assertThat(actual.getUsername()).isEqualTo(user.getUsername());
        assertThat(actual.getPassword()).isEqualTo(user.getPassword());
        assertThat(actual.getAuthorities()).isEqualTo(user.getRole().getPermissions().stream().map(
                permission -> new SimpleGrantedAuthority(permission.name())
        ).collect(Collectors.toSet()));
    }

    @Test
    void getAllUsers_shouldReturnValidData() {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        when(appUserRepository.findAllByOrderByTimeStampDesc(pageable))
                .thenReturn(Page.empty(Pageable.ofSize(1)));

        //when
        Page<UserResponseDTO> actual = underTest.getAllUsers(pageable);

        //then
        verify(appUserRepository).findAllByOrderByTimeStampDesc(pageable);
        assertThat(pageable.getPageSize()).isEqualTo(actual.getSize());
    }

    @Test
    void getUserById_shouldThrowExceptionWhenIdIsInvalid() {
        //given
        Long userId = 1L;
        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("id")
                .hasMessageContaining(userId.toString());
    }

    @Test
    void getUserById_shouldReturnValidUserWhenIdIsValid() {
        //given
        Long userId = 1L;
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(new AppUserEntity()));

        //when
        UserResponseDTO actual = underTest.getUserById(userId);

        //then
        verify(appUserRepository).findById(userId);
        assertThat(actual).isNotNull();
    }

    @Test
    @WithMockUser(username = "henk")
    void updateUser_shouldThrowExceptionWhenImageIdIsInvalid() {
        //given
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Collections.emptySet()).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .username("henk")
                .role(role)
                .build();
        user.setId(1L);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(imageRepository.findById(request.getImageId())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(request, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Image")
                .hasMessageContaining("id")
                .hasMessageContaining(request.getImageId().toString());
        verify(imageRepository).findById(request.getImageId());
    }

    @Test
    @WithMockUser(username = "henk")
    void updateUser_shouldThrowExceptionWhenUserDoesNotHaveValidPermission() {
        //given
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Collections.emptySet()).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .username("henk")
                .role(role)
                .build();
        user.setId(4L);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(request, userId))
                .isInstanceOf(LimitedPermissionException.class);
    }

    @Test
    @WithMockUser(username = "henk")
    void updateUser_shouldThrowExceptionWhenUserRequest() {
        //given
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Set.of(MONITOR_USER)).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .username("henk")
                .role(role)
                .build();
        user.setId(userId);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(imageRepository.findById(request.getImageId())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(request, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(request.getImageId().toString());
    }

    @Test
    @WithMockUser(username = "henk")
    void updateUser_shouldThrowExceptionWhenUserWithRequestedUsernameExists() {
        //given
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Set.of(MONITOR_USER)).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .username("henk")
                .role(role)
                .build();
        user.setId(4L);

        ImageEntity image = new ImageEntity();
        image.setId(6L);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(imageRepository.findById(request.getImageId())).thenReturn(Optional.of(image));
        when(appUserRepository.existsByUsername(request.getUsername())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(request, userId))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining(request.getUsername());
    }

    @Test
    @WithMockUser(username = "henk")
    void updateUser_shouldReturnValidDataWhenRequestIsValid() {
        //given
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Set.of(MONITOR_USER)).build();
        role.setId(4L);
        AppUserEntity currentUser = AppUserEntity
                .builder()
                .name("Henk")
                .username("henk")
                .role(role)
                .build();
        currentUser.setId(1L);

        AppUserEntity requestedUser = new AppUserEntity();
        currentUser.setId(5L);

        ImageEntity image = new ImageEntity();
        image.setId(6L);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(requestedUser));
        when(appUserRepository.findAppUserByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));
        when(imageRepository.findById(request.getImageId())).thenReturn(Optional.of(image));
        when(appUserRepository.save(requestedUser)).thenReturn(requestedUser);

        //when
        UserResponseDTO actual = underTest.updateUser(request, userId);

        //then
        verify(appUserRepository).save(requestedUser);

        assertThat(actual.getName()).isEqualTo(request.getName());
        assertThat(actual.getUsername()).isEqualTo(request.getUsername());
        assertThat(actual.getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    @WithMockUser(username = "niko")
    void setImageToUser_shouldThrowExceptionWhenUserIdIsInvalid() {
        //given
        Long userId = 1L;
        SetUserImageRequest imageDTO = new SetUserImageRequest();
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Collections.emptySet()).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .role(role)
                .build();
        user.setId(2L);

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.setImageToUser(userId, imageDTO))
                .isInstanceOf(LimitedPermissionException.class);
        verify(appUserRepository).findAppUserByUsername(user.getUsername());
    }

    @Test
    @WithMockUser(username = "niko")
    void setImageToUser_shouldThrowExceptionWhenUserDoesNotHaveRequiredPermission() {
        //given
        Long userId = 1L;
        SetUserImageRequest imageDTO = new SetUserImageRequest();
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Collections.emptySet()).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .role(role)
                .build();
        user.setId(2L);

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.setImageToUser(userId, imageDTO))
                .isInstanceOf(LimitedPermissionException.class);
        verify(appUserRepository).findAppUserByUsername(user.getUsername());
    }

    @Test
    @WithMockUser(username = "niko")
    void setImageToUser_shouldThrowExceptionWhenRequestedImageIsInvalid() {
        //given
        Long userId = 1L;
        SetUserImageRequest imageDTO = new SetUserImageRequest();
        imageDTO.setImageId(2L);
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Set.of(MONITOR_USER)).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .role(role)
                .build();
        user.setId(3L);

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(imageRepository.findById(imageDTO.getImageId())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.setImageToUser(userId, imageDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Image")
                .hasMessageContaining("id")
                .hasMessageContaining(imageDTO.getImageId().toString());
        verify(appUserRepository).findAppUserByUsername(user.getUsername());
        verify(imageRepository).findById(imageDTO.getImageId());
    }

    @Test
    @WithMockUser(username = "niko")
    void setImageToUser_shouldReturnValidDataWhenRequestIsValid() {
        //given
        Long userId = 1L;
        SetUserImageRequest imageDTO = new SetUserImageRequest();
        imageDTO.setImageId(2L);
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Set.of(MONITOR_USER)).build();
        role.setId(3L);
        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .role(role)
                .build();
        user.setId(userId);
        ImageEntity image = ImageEntity.builder().imageName("name").url("url").build();
        image.setId(4L);

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(imageRepository.findById(imageDTO.getImageId())).thenReturn(Optional.of(image));
        when(appUserRepository.save(user)).thenReturn(user);

        //when
        UserResponseDTO actual = underTest.setImageToUser(userId, imageDTO);

        //then
        verify(appUserRepository).findAppUserByUsername(user.getUsername());
        verify(appUserRepository).findById(userId);
        verify(imageRepository).findById(imageDTO.getImageId());
        verify(appUserRepository).save(user);
        assertThat(actual.getUsername()).isEqualTo(user.getUsername());
        assertThat(actual.getName()).isEqualTo(user.getName());
        assertThat(actual.getRole().getId()).isEqualTo(user.getRole().getId());
        assertThat(actual.getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    void setRoleToUser_throwsExceptionWhenRoleIdIsInvalid() {
        //given
        Long userId = 1L;
        SetUseRoleRequest roleDTO = new SetUseRoleRequest(2L);
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(new AppUserEntity()));
        when(appRoleRepository.findById(roleDTO.getRoleId())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.setRoleToUser(userId, roleDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Role")
                .hasMessageContaining("id")
                .hasMessageContaining(roleDTO.getRoleId().toString());
        verify(appUserRepository).findById(userId);
        verify(appRoleRepository).findById(roleDTO.getRoleId());
    }

    @Test
    void setRoleToUser_shouldReturnValidDataWhenRequestIsValid() {
        //given
        Long userId = 1L;
        SetUseRoleRequest roleDTO = new SetUseRoleRequest(2L);
        AppRoleEntity role = AppRoleEntity.builder().roleName("test").permissions(Collections.emptySet()).build();
        role.setId(2L);
        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .password("pass")
                .build();
        user.setId(userId);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appRoleRepository.findById(roleDTO.getRoleId())).thenReturn(Optional.of(role));
        when(appUserRepository.save(user)).thenReturn(user);

        //when
        UserResponseDTO actual = underTest.setRoleToUser(userId, roleDTO);

        //then
        verify(appUserRepository).findById(userId);
        verify(appRoleRepository).findById(roleDTO.getRoleId());
        verify(appUserRepository).save(user);
        assertThat(actual.getName()).isEqualTo(user.getName());
        assertThat(actual.getUsername()).isEqualTo(user.getUsername());
        assertThat(actual.getRole().getId()).isEqualTo(role.getId());
    }

    @Test
    @WithMockUser(username = "niko")
    void deleteUser_throwsExceptionWhenUserIdIsInvalid() {
        //given
        Long userId = 1L;
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Collections.emptySet()).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .role(role)
                .build();
        user.setId(2L);
        when(appUserRepository.existsById(userId)).thenReturn(false);

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteUser(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("id")
                .hasMessageContaining(userId.toString());
        verify(appUserRepository).existsById(userId);
    }

    @Test
    @WithMockUser(username = "niko")
    void deleteUser_throwsExceptionWhenUserDoesNotHavePermission() {
        //given
        Long userId = 1L;
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Collections.emptySet()).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .role(role)
                .build();
        user.setId(2L);
        when(appUserRepository.existsById(userId)).thenReturn(true);
        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteUser(userId))
                .isInstanceOf(LimitedPermissionException.class);
        verify(appUserRepository).findAppUserByUsername(user.getUsername());
    }

    @Test
    @WithMockUser(username = "niko")
    void deleteUser_returnValidDataWhenUserHaveRequiredPermission() {
        //given
        Long userId = 1L;
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Set.of(MONITOR_USER)).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .role(role)
                .build();
        user.setId(2L);
        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(appUserRepository.existsById(userId)).thenReturn(true);

        //when
        underTest.deleteUser(userId);

        //then
        verify(appUserRepository).findAppUserByUsername(user.getUsername());
        verify(appUserRepository).existsById(userId);
        verify(appUserRepository).deleteById(userId);
    }

    @Test
    @WithMockUser(username = "philly")
    void updatePassword_throwsExceptionWhenUserIdIsInvalid() {
        //given
        Long userId = 1L;
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                "old_password", "new_password", "new_Password"
        );

        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .password("pass")
                .build();

        AppUserEntity currentUser = AppUserEntity
                .builder()
                .name("Phil")
                .username("philly")
                .password("pass")
                .build();
        currentUser.setId(2L);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));

        //when
        //then
        assertThatThrownBy(() -> underTest.updatePassword(request, userId))
                .isInstanceOf(LimitedPermissionException.class);
    }

    @Test
    @WithMockUser(username = "philly")
    void updatePassword_throwsExceptionWhenNewPasswordNotConfirmed() {
        //given
        Long userId = 1L;
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                "old_password", "new_password", "new_Password"
        );

        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .password("pass")
                .build();

        AppUserEntity currentUser = AppUserEntity
                .builder()
                .name("Phil")
                .username("philly")
                .password("pass")
                .build();
        currentUser.setId(userId);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));

        //when
        //then
        assertThatThrownBy(() -> underTest.updatePassword(request, userId))
                .isInstanceOf(PasswordMismatchException.class)
                .hasMessageContaining(request.getNewPassword())
                .hasMessageContaining(request.getConfirmNewPassword());
        verify(appUserRepository).findById(userId);
        verify(appUserRepository).findAppUserByUsername(currentUser.getUsername());
    }

    @Test
    @WithMockUser(username = "philly")
    void updatePassword_throwsExceptionWhenOldAndNewPasswordAreEqual() {
        //given
        Long userId = 1L;
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                "old_password", "old_password", "old_password"
        );

        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .password("pass")
                .build();

        AppUserEntity currentUser = AppUserEntity
                .builder()
                .name("Phil")
                .username("philly")
                .password("pass")
                .build();
        currentUser.setId(userId);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));

        //when
        //then
        assertThatThrownBy(() -> underTest.updatePassword(request, userId))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("passwords")
                .hasMessageContaining(request.getOldPassword())
                .hasMessageContaining(request.getNewPassword());
        verify(appUserRepository).findById(userId);
        verify(appUserRepository).findAppUserByUsername(currentUser.getUsername());
    }

    @Test
    @WithMockUser(username = "philly")
    void updatePassword_throwsExceptionWhenOldPasswordIsWrong() {
        //given
        Long userId = 1L;
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                "old_password", "new_password", "new_password"
        );

        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .password("pass")
                .build();

        AppUserEntity currentUser = AppUserEntity
                .builder()
                .name("Phil")
                .username("philly")
                .password("pass")
                .build();
        currentUser.setId(userId);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));

        //when
        //then
        assertThatThrownBy(() -> underTest.updatePassword(request, userId))
                .isInstanceOf(PasswordMismatchException.class)
                .hasMessageContaining(request.getOldPassword())
                .hasMessageContaining("user's password");
        verify(appUserRepository).findById(userId);
        verify(appUserRepository).findAppUserByUsername(currentUser.getUsername());
    }

    @Test
    @WithMockUser(username = "philly")
    void updatePassword_returnValidDataWhenRequestIsValid() {
        //given
        Long userId = 1L;
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                "old_password", "new_password", "new_password"
        );

        AppUserEntity user = AppUserEntity
                .builder()
                .name("Nicola")
                .username("niko")
                .password("old_pass")
                .build();

        AppUserEntity currentUser = AppUserEntity
                .builder()
                .name("Phil")
                .username("philly")
                .password("pass")
                .build();
        currentUser.setId(userId);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encoded_new_password");
        when(appUserRepository.save(user)).thenReturn(user);

        //when
        underTest.updatePassword(request, userId);

        //then
        ArgumentCaptor<AppUserEntity> userArgCaptor = ArgumentCaptor.forClass(AppUserEntity.class);
        verify(appUserRepository).save(userArgCaptor.capture());
        AppUserEntity actual = userArgCaptor.getValue();
        assertThat(actual.getPassword()).isEqualTo(passwordEncoder.encode(request.getNewPassword()));
        verify(appUserRepository).findById(userId);
        verify(appUserRepository).findAppUserByUsername(currentUser.getUsername());
    }

    @Test
    void userExistsWithUsername_returnFalseWhenUserNameIsInvalid() {
        //given
        String username = "test";
        when(appUserRepository.existsByUsername(username)).thenReturn(false);

        //when
        boolean actual = underTest.existsWithUsername(username);

        //then
        verify(appUserRepository).existsByUsername(username);
        assertThat(actual).isFalse();
    }

    @Test
    void userExistsWithUsername_returnTrueWhenUserNameIsValid() {
        //given
        String username = "test";
        when(appUserRepository.existsByUsername(username)).thenReturn(true);

        //when
        boolean actual = underTest.existsWithUsername(username);

        //then
        verify(appUserRepository).existsByUsername(username);
        assertThat(actual).isTrue();
    }

    @Test
    void getUserByUsername_throwsExceptionWhenUsernameIsInvalid() {
        //given
        String username = "test";
        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getUserByUsername(username, false))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining(username);
    }

    @Test
    void getUserByUsername_shouldReturnUserWithPermissionsWhenUsernameIsValid() {
        //given
        String username = "test";
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Set.of(MONITOR_USER)).build();
        AppUserEntity user = AppUserEntity
                .builder()
                .username(username)
                .role(role)
                .build();
        when(appUserRepository.findAppUserByUsernameWithPermission(username)).thenReturn(Optional.of(user));

        //when
        AppUserEntity actual = underTest.getUserByUsername(username, true);

        //then
        verify(appUserRepository).findAppUserByUsernameWithPermission(username);
        assertThat(actual.getUsername()).isEqualTo(username);
        assertThat(actual.getRole().getPermissions()).contains(Permissions.MONITOR_USER);
    }

    @Test
    void getUserByUsername_shouldReturnUserWithoutPermissionsWhenUsernameIsValid() {
        //given
        String username = "test";
        AppUserEntity user = AppUserEntity
                .builder()
                .username(username)
                .role(new AppRoleEntity())
                .build();
        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(user));

        //when
        AppUserEntity actual = underTest.getUserByUsername(username, false);

        //then
        verify(appUserRepository).findAppUserByUsername(username);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(actual.getRole().getPermissions()).isEmpty();
    }
}