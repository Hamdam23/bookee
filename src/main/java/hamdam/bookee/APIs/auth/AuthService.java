package hamdam.bookee.APIs.auth;

import hamdam.bookee.APIs.user.AppUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {
    AppUser addUser(AuthUserDTO user);

    void generateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
