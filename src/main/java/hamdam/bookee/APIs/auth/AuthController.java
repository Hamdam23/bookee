package hamdam.bookee.APIs.auth;

import hamdam.bookee.APIs.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static hamdam.bookee.tools.constants.Endpoints.API_TOKEN_REFRESH;
import static hamdam.bookee.tools.constants.Endpoints.API_REGISTER;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(API_REGISTER)
    public AppUser register(@RequestBody RegistrationRequest user) {
        return authService.registerUser(user);
        // TODO: 9/2/22 return tokens also, because currently user is have to login again after registration
    }

    // TODO: 31/07/22 required: void ni yoqotish
    // TODO: 31/07/22 required: replace request with header only
    @GetMapping(API_TOKEN_REFRESH)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: 9/2/22 don't pass request and response to authService
        authService.refreshToken(request, response);
    }
}
