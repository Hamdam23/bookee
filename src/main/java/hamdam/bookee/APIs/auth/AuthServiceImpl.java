package hamdam.bookee.APIs.auth;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.token.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static hamdam.bookee.tools.token.TokenChecker.checkHeader;
import static hamdam.bookee.tools.token.TokenUtils.getTokenResponse;
import static hamdam.bookee.tools.token.TokenUtils.getUsernameFromToken;

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
    public TokensResponse refreshToken(String header) {
        checkHeader(header, false);

        try {
            UserDetails user = userService.loadUserByUsername(getUsernameFromToken(header, false));
            return TokenUtils.getAccessTokenResponse(user.getUsername(), userRepository);
        } catch (AlgorithmMismatchException exception) {
            throw new AlgorithmMismatchException(exception.getMessage());
        }
    }
}
