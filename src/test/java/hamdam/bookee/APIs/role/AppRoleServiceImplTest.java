package hamdam.bookee.APIs.role;

import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.DuplicateResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppRoleServiceImplTest {

    @Mock
    private AppRoleRepository appRoleRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppRoleServiceImpl underTest;

    @Test
    void canCreateRole() {
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
    void canThrowExceptionWhenRoleNameIsDuplicate() {
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
    void canReturnValidDataWhenCalled() {
        //given
        when(appRoleRepository.findAllByOrderByTimeStampDesc(any())).thenReturn(Page.empty());

        //when
        underTest.getAllRoles(PageRequest.of(0, 10));

        //then
        verify(appRoleRepository).findAllByOrderByTimeStampDesc(PageRequest.of(0, 10));
    }

    @Test
    void shouldReturnValidDataWhenIdIsValid() {
        //given
        Long id = 1L;
        AppRoleEntity role = new AppRoleEntity("USER", Set.of(MONITOR_ROLE));
        AppUserEntity user = new AppUserEntity("Hamdam", role);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
        SecurityContextHolder.setContext(context);

        when(appRoleRepository.existsById(id)).thenReturn(true);
        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.of(user));

        //when
        ApiResponse actual = underTest.deleteRoleById(id);

        //then
        verify(appRoleRepository).deleteById(id);
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}