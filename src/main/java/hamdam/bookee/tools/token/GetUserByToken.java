package hamdam.bookee.tools.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class GetUserByToken {

    public static AppUserEntity getUserByRequest(HttpServletRequest request, AppUserRepository userRepository) {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = TokenUtils.decodeToken(token, true);
        String username = decodedJWT.getSubject();

        return userRepository.findAppUserByUserName(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
    }
}
