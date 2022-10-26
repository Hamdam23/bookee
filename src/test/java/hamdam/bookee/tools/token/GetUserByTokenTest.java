package hamdam.bookee.tools.token;

import hamdam.bookee.APIs.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

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