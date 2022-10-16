package hamdam.bookee.APIs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.token.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static hamdam.bookee.tools.token.TokenChecker.checkHeader;
import static hamdam.bookee.tools.token.TokenUtils.getTokenResponse;
import static hamdam.bookee.tools.token.TokenUtils.getUsernameFromToken;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // TODO remove userRepository ???? getTokenResponse needs userRepo
    private final AppUserRepository userRepository;
    private final AppUserService userService;

    @Override
    public TokensResponse registerUser(RegistrationRequest request) {
        userService.saveUser(request);
        return getTokenResponse(request.getUsername(), userRepository);
    }

    @Override
    public TokensResponse refreshToken(String header) throws IOException {
        checkHeader(header, false);

        try {
            UserDetails user = userService.loadUserByUsername(getUsernameFromToken(header));
            TokensResponse accessTokenResponse = TokenUtils.getAccessTokenResponse(user.getUsername(), userRepository);
            TokenUtils.presentToken(accessTokenResponse, response);
        } catch (Exception exception) {
//            response.setHeader("error", exception.getMessage());
//            response.setStatus(FORBIDDEN.value());
//            Map<String, String> error = new HashMap<>();
//            error.put("error_error_message", exception.getMessage());
//            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
//            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}
