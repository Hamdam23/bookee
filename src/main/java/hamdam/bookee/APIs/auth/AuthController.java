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

    /**
     * It takes a RegistrationRequest object, passes it to the authService, and returns a TokensResponse object
     *
     * @param user The user object that is passed in the request body.
     * @return TokensResponse
     */
    @PostMapping(API_REGISTER)
    public TokensResponse register(@RequestBody RegistrationRequest user) {
        return authService.registerUser(user);
    }

    /**
     * It takes the authorization header from the request, and passes it to the authService.refreshToken() function
     *
     * @param header The header is the authorization header that is sent with the request.
     * @return TokensResponse
     */
    @GetMapping(API_TOKEN_REFRESH)
    public TokensResponse refreshToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String header) {
        return authService.refreshToken(header);
    }
}
