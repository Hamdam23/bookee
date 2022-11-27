package hamdam.bookee.APIs.role;

import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.DuplicateResourceException;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.GET_USER;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SecurityTestExecutionListeners
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class AppRoleServiceImplTest {

    @Mock
    private AppRoleRepository appRoleRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppRoleServiceImpl underTest;

    @Test
    void deleteRoleById_shouldCreateRole() {
        // given
        AppRoleRequestDTO requestDTO = new AppRoleRequestDTO(
                "USER", true, Collections.emptySet()
        );
        when(appRoleRepository.save(any())).thenReturn(new AppRoleEntity(requestDTO));

        // when
        AppRoleResponseDTO actual = underTest.addRole(requestDTO);

        // then
        assertThat(actual.getRoleName()).isEqualTo(requestDTO.getRoleName());
        assertThat(actual.isDefault()).isEqualTo(requestDTO.isDefault());
        assertThat(actual.getPermissions()).isEqualTo(requestDTO.getPermissions());
    }

    @Test
    void deleteRoleById_shouldThrowExceptionWhenRoleNameIsDuplicate() {
        // given
        AppRoleRequestDTO requestDTO = new AppRoleRequestDTO(
                "USER", true, Collections.emptySet()
        );
        when(appRoleRepository.existsByRoleName(any())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.addRole(requestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("role name");
    }

    @Test
    void deleteRoleById_shouldReturnValidDataWhenRequestIsValid() {
        //given
        when(appRoleRepository.findAllByOrderByTimeStampDesc(any())).thenReturn(Page.empty());

        //when
        underTest.getAllRoles(PageRequest.of(0, 10));

        //then
        verify(appRoleRepository).findAllByOrderByTimeStampDesc(PageRequest.of(0, 10));
    }

    @Test
    @WithMockUser(username = "Hamdam")
    void deleteRoleById_shouldThrowExceptionWhenUserDoesNotHaveValidPermission() {
        //given
        Long id = 1L;
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(GET_USER));
        AppUserEntity user = new AppUserEntity("Hamdam", role);

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteRoleById(id))
                .isInstanceOf(LimitedPermissionException.class);
    }

    @Test
    @WithMockUser(username = "Hamdam")
    void deleteRoleById_shouldThrowExceptionWhenRoleIdIsInvalid() {
        Long id = 1L;
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_ROLE));
        AppUserEntity user = new AppUserEntity("Hamdam", role);

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteRoleById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Role");
    }

    @Test
    @WithMockUser(username = "Hamdam")
    void deleteRoleById_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        Long id = 1L;
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_ROLE));
        AppUserEntity user = new AppUserEntity("Hamdam", role);

        when(appRoleRepository.existsById(id)).thenReturn(true);
        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        underTest.deleteRoleById(id);

        //then
        verify(appRoleRepository).deleteById(id);
    }
}