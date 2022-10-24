package hamdam.bookee.APIs.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static hamdam.bookee.tools.constants.Endpoints.API_REGISTER;
import static hamdam.bookee.tools.constants.Endpoints.API_TOKEN_REFRESH;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(API_REGISTER)
    public TokensResponse register(@RequestBody RegistrationRequest user) {
        return authService.registerUser(user);
    }

    @GetMapping(API_TOKEN_REFRESH)
    public TokensResponse refreshToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String header) {
        return authService.refreshToken(header);
    }
}
