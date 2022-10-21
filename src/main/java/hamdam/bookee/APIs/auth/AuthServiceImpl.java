package hamdam.bookee.APIs.auth;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static hamdam.bookee.tools.token.TokenChecker.checkHeader;
import static hamdam.bookee.tools.token.TokenUtils.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserService userService;

    @Override
    public TokensResponse registerUser(RegistrationRequest request) {
        userService.saveUser(request);
        return getTokenResponse(userService.getUserByUsername(request.getUsername()));
    }

    @Override
    public TokensResponse refreshToken(String header) {
        checkHeader(header, false);

        try {
            AppUserEntity user = userService.getUserByUsername(getUsernameFromToken(header, false));
            return getAccessTokenResponse(user);
        } catch (AlgorithmMismatchException exception) {
            throw new AlgorithmMismatchException(exception.getMessage());
        }
    }
}
