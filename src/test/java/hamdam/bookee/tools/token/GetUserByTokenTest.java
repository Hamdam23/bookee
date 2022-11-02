package hamdam.bookee.tools.token;

import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
class GetUserByTokenTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private GetUserByToken underTest;

    @Test
    @WithMockUser(username = "philly")
    void getUserByRequest_throwsExceptionWhenUserNameIsInvalid() {
        //given
        AppUserEntity user = new AppUserEntity("Phil", "philly", "very_secret_password");

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> GetUserByToken.getUserByRequest(appUserRepository))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(user.getUsername());
        verify(appUserRepository).findAppUserByUsername(user.getUsername());
    }

    @Test
    @WithMockUser(username = "philly")
    void getUserByRequest_returnValidDataWhenRequestIsValid() {
        //given
        AppUserEntity user = new AppUserEntity("Phil", "philly", "very_secret_password");

        when(appUserRepository.findAppUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        AppUserEntity actual = GetUserByToken.getUserByRequest(appUserRepository);

        //then
        assertThat(actual.getUsername()).isEqualTo(user.getUsername());
    }

}