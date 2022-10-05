package hamdam.bookee.tools.token;

import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.AppUserServiceImpl;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = GetUserByToken.class)
class GetUserByTokenTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Autowired
    GetUserByToken underTest;

    @Test
    void throwsExceptionWhenUserNameIsInvalid() {
        //given
//        AppUserEntity user = new AppUserEntity("Phil", "philly", "very_secret_password");
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUserName(), null));
//        SecurityContextHolder.setContext(context);
//        when(appUserRepository.findAppUserByUserName(user.getUserName())).thenReturn(Optional.empty());
//
//        //when
//        //then
//        assertThatThrownBy(() -> underTest.getClass(appUserRepository))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("User")
//                .hasMessageContaining("username")
//                .hasMessageContaining(user.getUserName());
//        verify(appUserRepository).findAppUserByUserName(user.getUserName());
    }

}