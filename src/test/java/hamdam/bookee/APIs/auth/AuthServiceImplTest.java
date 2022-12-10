package hamdam.bookee.APIs.auth;

import hamdam.bookee.APIs.role.helpers.RoleMappers;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.APIs.user.helpers.UserMappers;
import hamdam.bookee.tools.utils.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.GET_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AppUserService userService;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl underTest;

    @Test
    void registerUser_returnValidResponseWhenRequestIsValid() {
        //given
        String username = "niko";
        AppUserEntity user = UserMappers.mapToAppUserEntity(username, RoleMappers.mapToAppRoleEntity("role"));
        when(userService.getUserByUsername(username, true)).thenReturn(user);
        when(tokenProvider.getTokenResponse(user)).thenReturn(
                new TokensResponse("a", "b", "c", "d", "e", Set.of(GET_USER)));

        //when
        TokensResponse actual = underTest.registerUser(new RegistrationRequest("Nikola", username, "pass"));

        //then
        verify(userService).getUserByUsername(username, true);
        verify(tokenProvider).getTokenResponse(user);
        assertThat(actual.getAccessToken()).isNotBlank();
        assertThat(actual.getRefreshToken()).isNotBlank();
    }

//    @Test
//    void refreshToken_returnValidResponseWhenRequestIsValid() {
//        //given
//        String header = "Bearer ";
//        AppUserEntity user = new AppUserEntity("niko", new AppRoleEntity("role"));
//        when(tokenProvider.getUsernameFromToken(header, false)).thenReturn();
//
//        //when
//
//        //then
//    }

}