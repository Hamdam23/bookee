package hamdam.bookee.APIs.auth;

import hamdam.bookee.APIs.user.AppUserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {
    TokensResponse registerUser(RegistrationRequest user);

    // TODO: 9/2/22 why request and response objects in Service (logic) layer?
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
