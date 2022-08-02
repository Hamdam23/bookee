package hamdam.bookee.APIs.auth;

import hamdam.bookee.APIs.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static hamdam.bookee.tools.constants.Endpoints.API_REFRESH_TOKEN;
import static hamdam.bookee.tools.constants.Endpoints.API_REGISTER;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(API_REGISTER)
    public AppUser register(@RequestBody AuthUserDTO user) {
        return authService.addUser(user);
    }

    // TODO: 31/07/22 required: void ni yoqotish
    // TODO: 31/07/22 optional: replace request with header only
    @GetMapping(API_REFRESH_TOKEN)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.generateRefreshToken(request, response);
    }
}
