package hamdam.bookee.APIs.auth;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.jwt_token.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static hamdam.bookee.tools.utils.TokenUtils.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserService userService;

    @Override
    public TokensResponse registerUser(RegistrationRequest request) {
        userService.saveUser(request);
        return getTokenResponse(userService.getUserByUsername(request.getUsername(), true));
    }

    @Override
    public TokensResponse refreshToken(String header) {
        try {
            checkHeader(header, false);
            AppUserEntity user = userService.getUserByUsername(getUsernameFromToken(header, false), false);
            return getAccessTokenResponse(user);
        } catch (MissingTokenException exception) {
            throw exception;
        } catch (ResourceNotFoundException exception) {
            throw new UserTokenException();
        } catch (AlgorithmMismatchException exception) {
            throw new AlgorithmMismatchTokenException();
        } catch (SignatureVerificationException exception) {
            throw new SignatureTokenException();
        } catch (TokenExpiredException exception) {
            throw new ExpiredTokenException();
        } catch (Exception exception) {
            throw new UnknownTokenException();
        }
    }
}
