package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.SetUserRoleDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        AppUserEntity expected = new AppUserEntity(
                "Nicola", "niko", "very_secret_password"
        );
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(expected));

        //when
        AppUserEntity actual = underTest.getAppUserById(userId);

        //then
        verify(appUserRepository).findById(userId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllUsers_shouldReturnSingleUserWhenSingleUserAvailable() {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        when(appUserRepository.findAllByOrderByTimeStampDesc(pageable))
                .thenReturn(Page.empty(Pageable.ofSize(1)));

        //when
        Page<AppUserResponseDTO> actual = underTest.getAllUsers(pageable);

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
        AppUserResponseDTO actual = underTest.getUserById(userId);

        //then
        verify(appUserRepository).findById(userId);
        assertThat(actual).isNotNull();
    }

    @Test
    void updateUser_shouldThrowExceptionRequestedRoleIdIsValid() {
        //given
        Long userId = 1L;
        AppUserRequestDTO request = new AppUserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_USER));
        AppUserEntity user = new AppUserEntity("Phil", role);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(appRoleRepository.findById(request.getRoleId())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(request, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Role")
                .hasMessageContaining("id")
                .hasMessageContaining(request.getRoleId().toString());
        verify(appRoleRepository).findById(request.getRoleId());
    }

    @Test
    void updateUser_shouldThrowExceptionWhenImageIdIsInvalid() {
        //given
        Long userId = 1L;
        AppUserRequestDTO request = new AppUserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_ROLE));
        AppUserEntity user = new AppUserEntity("Phil", role);
        user.setId(1L);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));
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
    void updateUser_shouldThrowExceptionWhenUserDoesNotHavePermission() {
        //given
        Long userId = 1L;
        AppUserRequestDTO request = new AppUserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_ROLE));
        AppUserEntity user = new AppUserEntity("Phil", role);
        user.setId(4L);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(request, userId))
                .isInstanceOf(LimitedPermissionException.class)
                .hasMessageContaining("access");
    }

    @Test
    void updateUser_shouldReturnValidDataWhenResponseIsValid() {
        //given
        Long userId = 1L;
        AppUserRequestDTO request = new AppUserRequestDTO(
                "Nicola",
                "niko",
                2L,
                3L
        );
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_USER));
        role.setId(4L);
        AppUserEntity currentUser = new AppUserEntity("Phil", "philly", role);
        currentUser.setId(1L);

        AppUserEntity requestedUser = new AppUserEntity();
        currentUser.setId(5L);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(currentUser.getUserName(), null));
        SecurityContextHolder.setContext(context);

        ImageEntity image = new ImageEntity();
        image.setId(6L);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(requestedUser));
        when(appUserRepository.findAppUserByUserName(currentUser.getUserName())).thenReturn(Optional.of(currentUser));
        when(appRoleRepository.findById(request.getRoleId())).thenReturn(Optional.of(role));
        when(imageRepository.findById(request.getImageId())).thenReturn(Optional.of(image));
        when(appUserRepository.save(requestedUser)).thenReturn(requestedUser);

        //when
        AppUserResponseDTO actual = underTest.updateUser(request, userId);

        //then
        verify(appUserRepository).findById(userId);
        verify(appUserRepository).findAppUserByUserName(currentUser.getUserName());
        verify(appRoleRepository).findById(request.getRoleId());
        verify(imageRepository).findById(request.getImageId());
        verify(appUserRepository).save(requestedUser);

        assertThat(actual.getName()).isEqualTo(request.getName());
        assertThat(actual.getUserName()).isEqualTo(request.getUserName());
        assertThat(actual.getRole().getId()).isEqualTo(role.getId());
        assertThat(actual.getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    void setImageToUser_shouldThrowExceptionWhenUserNameIsInvalid() {
        //given
        Long userId = 1L;
        UserImageDTO imageDTO = new UserImageDTO();
        AppUserEntity user = new AppUserEntity("Nicola",
                "niko",
                "very_secret_password");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);

        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.setImageToUser(userId, imageDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("username")
                .hasMessageContaining(user.getUserName());
        verify(appUserRepository).findAppUserByUserName(user.getUserName());
    }

    @Test
    void setImageToUser_shouldThrowExceptionWhenUserHasLimitedPermission() {
        //given
        Long userId = 1L;
        UserImageDTO imageDTO = new UserImageDTO();
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_ROLE));
        AppUserEntity user = new AppUserEntity("Nicola",
                "niko",
                role);
        user.setId(2L);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);

        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.setImageToUser(userId, imageDTO))
                .isInstanceOf(LimitedPermissionException.class)
                .hasMessageContaining("access");
        verify(appUserRepository).findAppUserByUserName(user.getUserName());
    }

    @Test
    void setImageToUser_shouldThrowExceptionWhenRequestedImageIsInvalid() {
        //given
        Long userId = 1L;
        UserImageDTO imageDTO = new UserImageDTO();
        imageDTO.setImageId(2L);
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_USER));
        AppUserEntity user = new AppUserEntity("Nicola",
                "niko",
                role);
        user.setId(3L);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);

        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(imageRepository.findById(imageDTO.getImageId())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.setImageToUser(userId, imageDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Image")
                .hasMessageContaining("id")
                .hasMessageContaining(imageDTO.getImageId().toString());
        verify(appUserRepository).findAppUserByUserName(user.getUserName());
        verify(imageRepository).findById(imageDTO.getImageId());
    }

    @Test
    void setImageToUser_shouldReturnValidDataWhenRequestIsValid() {
        //given
        Long userId = 1L;
        UserImageDTO imageDTO = new UserImageDTO();
        imageDTO.setImageId(2L);
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_USER));
        role.setId(3L);
        AppUserEntity user = new AppUserEntity("Nicola",
                "niko",
                role);
        user.setId(userId);
        ImageEntity image = new ImageEntity(
                "alien", "solar-system/earth"
        );
        image.setId(4L);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);

        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(imageRepository.findById(imageDTO.getImageId())).thenReturn(Optional.of(image));
        when(appUserRepository.save(user)).thenReturn(user);

        //when
        AppUserResponseDTO actual = underTest.setImageToUser(userId, imageDTO);

        //then
        verify(appUserRepository).findAppUserByUserName(user.getUserName());
        verify(appUserRepository).findById(userId);
        verify(imageRepository).findById(imageDTO.getImageId());
        verify(appUserRepository).save(user);
        assertThat(actual.getUserName()).isEqualTo(user.getUserName());
        assertThat(actual.getName()).isEqualTo(user.getName());
        assertThat(actual.getRole().getId()).isEqualTo(user.getRole().getId());
        assertThat(actual.getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    void setRoleToUser_throwsExceptionWhenUserIdIsInvalid() {
        //given
        Long userId = 1L;
        SetUserRoleDTO roleDTO = new SetUserRoleDTO();
        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.setRoleToUser(userId, roleDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("id")
                .hasMessageContaining(userId.toString());
        verify(appUserRepository).findById(userId);
    }

    @Test
    void setRoleToUser_throwsExceptionWhenRoleIdIsInvalid() {
        //given
        Long userId = 1L;
        SetUserRoleDTO roleDTO = new SetUserRoleDTO(2L);
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
        SetUserRoleDTO roleDTO = new SetUserRoleDTO(2L);
        AppRoleEntity role = new AppRoleEntity(
                "test", Set.of(MONITOR_ROLE)
        );
        role.setId(2L);
        AppUserEntity user = new AppUserEntity("Nicola", "niko", "very_secret_password");
        user.setId(userId);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(appRoleRepository.findById(roleDTO.getRoleId())).thenReturn(Optional.of(role));
        when(appUserRepository.save(user)).thenReturn(user);

        //when
        AppUserResponseDTO actual = underTest.setRoleToUser(userId, roleDTO);

        //then
        verify(appUserRepository).findById(userId);
        verify(appRoleRepository).findById(roleDTO.getRoleId());
        verify(appUserRepository).save(user);
        assertThat(actual.getName()).isEqualTo(user.getName());
        assertThat(actual.getUserName()).isEqualTo(user.getUserName());
        assertThat(actual.getRole().getId()).isEqualTo(role.getId());
    }

    @Test
    void deleteUser_throwsExceptionWhenUserByTokenInvalid() {
        //given
        Long userId = 1L;
        AppUserEntity user = new AppUserEntity("Nicola",
                "niko",
                "very_secure_password");
        user.setId(userId);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);
        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteUser(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("username")
                .hasMessageContaining(user.getUserName());
        verify(appUserRepository).findAppUserByUserName(user.getUserName());
    }

    @Test
    void deleteUser_throwsExceptionWhenUserDoesNotHavePermission() {
        //given
        Long userId = 1L;
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_USER));
        AppUserEntity user = new AppUserEntity("Nicola",
                "niko",
                role);
        user.setId(userId);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);
        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteUser(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("username")
                .hasMessageContaining(user.getUserName());
        verify(appUserRepository).findAppUserByUserName(user.getUserName());
    }

    @Test
    void deleteUser_throwsExceptionWhenUserIdIsInvalid() {
        //given
        Long userId = 1L;
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_USER));
        AppUserEntity user = new AppUserEntity("Nicola",
                "niko",
                role);
        user.setId(2L);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);
        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(appUserRepository.existsById(userId)).thenReturn(false);

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteUser(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("id")
                .hasMessageContaining(userId.toString());
        verify(appUserRepository).findAppUserByUserName(user.getUserName());
        verify(appUserRepository).existsById(userId);
    }

    @Test
    void deleteUser_returnValidDataWhenUserIdIsInvalid() {
        //given
        Long userId = 1L;
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_USER));
        AppUserEntity user = new AppUserEntity("Nicola",
                "niko",
                role);
        user.setId(2L);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);
        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(appUserRepository.existsById(userId)).thenReturn(true);

        //when
        ApiResponse actual = underTest.deleteUser(userId);

        //then
        verify(appUserRepository).findAppUserByUserName(user.getUserName());
        verify(appUserRepository).existsById(userId);
        verify(appUserRepository).deleteById(userId);
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}