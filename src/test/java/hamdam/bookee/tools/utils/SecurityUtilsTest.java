package hamdam.bookee.tools.utils;

import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SecurityTestExecutionListeners
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class SecurityUtilsTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Test
    @WithMockUser(username = "philly")
    void getUserByRequest_throwsExceptionWhenUsernameIsInvalid() {
        //given
        String username = "philly";

        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> SecurityUtils.getUserByRequest(appUserRepository))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(username);
        verify(appUserRepository).findAppUserByUsername(username);
    }

    @Test
    @WithMockUser(username = "philly")
    void getUserByRequest_returnValidDataWhenRequestIsValid() {
        //given
        AppUserEntity user = new AppUserEntity("Phil", "philly", "very_secret_password");

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        AppUserEntity actual = SecurityUtils.getUserByRequest(appUserRepository);

        //then
        verify(appUserRepository).findAppUserByUsername(user.getUsername());
        assertThat(actual.getUsername()).isEqualTo(user.getUsername());
    }

}