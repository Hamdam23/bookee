package hamdam.bookee.APIs.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static hamdam.bookee.tools.constants.Endpoints.API_REGISTER;
import static hamdam.bookee.tools.constants.Endpoints.API_TOKEN_REFRESH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(API_REGISTER)
    public TokensResponse register(@RequestBody RegistrationRequest user) {
        return authService.registerUser(user);
    }

    @GetMapping(API_TOKEN_REFRESH)
    public TokensResponse refreshToken(@RequestHeader(value="Authorization") String header) throws IOException {
        return authService.refreshToken(header);
    }
}
