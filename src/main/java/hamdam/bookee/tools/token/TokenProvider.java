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
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static hamdam.bookee.tools.constants.ConstantFields.ROLE;

// TODO: 9/2/22 naming class as TokenProvider which contains only static methods is not a good idea
public class TokenProvider {

    // TODO: 9/2/22 get secret from environment variable
    private static final Algorithm accessAlgorithm = Algorithm.HMAC256("secret".getBytes());
    private static final Algorithm refreshAlgorithm = Algorithm.HMAC384("secret".getBytes());

    // TODO: 9/2/22 don't instantiate millis field here (needs discussion)
    private static final long millis = System.currentTimeMillis();
    // TODO: 9/2/22 needs better name
    private static final Date accTExpiry = new Date(millis + 3600000); // 1 hour = 3600000
    private static final Date refTExpiry = new Date(millis + 3600000 * 24 * 20); // 20 days

    public static DecodedJWT verifyToken(String token, boolean isAccessToken) {
        JWTVerifier verifier;
        if (isAccessToken) {
            verifier = JWT.require(accessAlgorithm).build();
        } else {
            verifier = JWT.require(refreshAlgorithm).build();
        }
        return verifier.verify(token);
    }

    // TODO: 9/2/22 do you need UserDetails here or only username?
    public static TokensResponse generateTokens(UserDetails user, AppUserRepository userRepository) {
        // TODO: 9/2/22 remove get() call
        AppRole role = userRepository.findAppUserByUserName(user.getUsername()).get().getRole();

        return new TokensResponse(
                buildAccessToken(user, role),
                accTExpiry,
                buildRefreshToken(user, role),
                refTExpiry,
                role.getRoleName(),
                role.getPermissions()
        );
    }

    // TODO: 9/2/22 needs better name
    public static AccessTResponse generateAToken(UserDetails user, AppUserRepository userRepository) {
        // TODO: 9/2/22 remove get() call
        AppRole role = userRepository.findAppUserByUserName(user.getUsername()).get().getRole();

        return new AccessTResponse(
                buildAccessToken(user, role),
                accTExpiry
        );
    }

    // TODO: 9/2/22 needs better name
    public static String buildAccessToken(UserDetails user, AppRole role) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(accTExpiry)
                .withClaim(ROLE, role.getRoleName())
                .sign(accessAlgorithm);
    }

    // TODO: 9/2/22 needs better name
    public static String buildRefreshToken(UserDetails user, AppRole role) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(refTExpiry)
                .withClaim(ROLE, role.getRoleName())
                .sign(refreshAlgorithm);
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
