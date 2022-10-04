package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void updateUser_shouldThrowExceptionWhenUserIdIsInvalid() {
        //given
        Long userId = 1L;
        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(new AppUserRequestDTO(), userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("id")
                .hasMessageContaining(userId.toString());
        verify(appUserRepository).findById(userId);
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
}