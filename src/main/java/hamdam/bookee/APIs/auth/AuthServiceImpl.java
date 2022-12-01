package hamdam.bookee.APIs.auth;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.jwt_token.*;
import hamdam.bookee.tools.utils.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserService userService;
    private final TokenProvider tokenProvider;

    @Override
    public TokensResponse registerUser(RegistrationRequest request) {
        // Saving the user and returning the token response.
        userService.saveUser(request);
        return tokenProvider.getTokenResponse(userService.getUserByUsername(request.getUsername(), true));
    }

    @Override
    public TokensResponse refreshToken(String header) {
        try {
            // It checks if the header is valid.
            tokenProvider.checkHeader(header, false);
            // Getting the user from the database using the username from the token.
            AppUserEntity user = userService.getUserByUsername(tokenProvider.getUsernameFromToken(header, false), false);
            // Creating a new access token for the user.
            return tokenProvider.getAccessTokenResponse(user);
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
