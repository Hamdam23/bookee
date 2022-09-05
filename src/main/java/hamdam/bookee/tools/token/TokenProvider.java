package hamdam.bookee.tools.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.auth.AccessTResponse;
import hamdam.bookee.APIs.auth.TokensResponse;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static hamdam.bookee.tools.constants.ConstantFields.ROLE;

// TODO: 9/2/22 naming class as TokenProvider which contains only static methods is not a good idea
public class TokenProvider {

    // TODO: 9/2/22 get secret from environment variable
    private static final Algorithm ACCESS_ALGORITHM = Algorithm.HMAC256("secret".getBytes());
    private static final Algorithm REFRESH_ALGORITHM = Algorithm.HMAC384("secret".getBytes());
    // TODO: 9/2/22 don't instantiate millis field here (needs discussion)
    // TODO: 9/2/22 needs better name
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static DecodedJWT verifyToken(String token, boolean isAccessToken) {
        JWTVerifier verifier;
        if (isAccessToken) {
            verifier = JWT.require(ACCESS_ALGORITHM).build();
        } else {
            verifier = JWT.require(REFRESH_ALGORITHM).build();
        }
        return verifier.verify(token);
    }

    // TODO: 9/2/22 do you need UserDetails here or only username?
    public static TokensResponse generateTokens(String username, AppUserRepository userRepository) {
        // TODO: 9/2/22 remove get() call
        AppRole role = userRepository.findAppUserByUserName(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        ).getRole();

        return new TokensResponse(
                buildAccessToken(username, role),
                DATE_FORMAT.format(new Date(System.currentTimeMillis() + 3600000)),
                buildRefreshToken(username, role),
                DATE_FORMAT.format(new Date(System.currentTimeMillis() + 3600000 * 24 * 20)),
                role.getRoleName(),
                role.getPermissions()
        );
    }

    // TODO: 9/2/22 needs better name
    public static AccessTResponse generateAToken(String username, AppUserRepository userRepository) {
        // TODO: 9/2/22 remove get() call
        AppRole role = userRepository.findAppUserByUserName(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        ).getRole();

        return new AccessTResponse(
                buildAccessToken(username, role),
                DATE_FORMAT.format(System.currentTimeMillis())
        );
    }

    // TODO: 9/2/22 needs better name
    public static String buildAccessToken(String username, AppRole role) {

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .withClaim(ROLE, role.getRoleName())
                .sign(ACCESS_ALGORITHM);
    }

    // TODO: 9/2/22 needs better name
    public static String buildRefreshToken(String username, AppRole role) {

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000 * 24 * 20))
                .withClaim(ROLE, role.getRoleName())
                .sign(REFRESH_ALGORITHM);
    }

    public static void sendTokens(TokensResponse tokensResponse, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> data = new ObjectMapper().convertValue(tokensResponse,
                new TypeReference<>() {
                }
        );
        new ObjectMapper().writeValue(response.getOutputStream(), data);
    }

    public static void sendAToken(AccessTResponse accessTResponse, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> data = new ObjectMapper().convertValue(accessTResponse,
                new TypeReference<>() {
                }
        );
        new ObjectMapper().writeValue(response.getOutputStream(), data);
    }
}
