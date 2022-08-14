package hamdam.bookee.tools.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.auth.TokenResponse;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.user.AppUserRepository;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.Map;

import static hamdam.bookee.tools.constants.ConstantFields.ROLE;

public class TokenProvider {
    private static final Algorithm accessAlgorithm = Algorithm.HMAC256("secret".getBytes());
    private static final Algorithm refreshAlgorithm = Algorithm.HMAC384("secret".getBytes());

    public static DecodedJWT verifyToken(String token, boolean isAccessToken) {

        JWTVerifier verifier;
        if (isAccessToken) {
            verifier = JWT.require(accessAlgorithm).build();
        } else {
            verifier = JWT.require(refreshAlgorithm).build();
        }
        return verifier.verify(token);
    }

    public static TokenResponse generateToken(UserDetails user, AppUserRepository userRepository){
        long millis = System.currentTimeMillis();
        Date acc_t_expiryDate = new Date(millis + 3600000); // 1 hour = 3600000
        Date ref_t_expiryDate = new Date(millis + 3600000 * 24 * 20); // 20 days

        AppRole role = userRepository.findAppUserByUserName(user.getUsername()).get().getRole();

        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(acc_t_expiryDate)
                .withClaim(ROLE, role.getRoleName())
                .sign(accessAlgorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(ref_t_expiryDate)
                .withClaim(ROLE, role.getRoleName())
                .sign(refreshAlgorithm);

        return new TokenResponse(
                access_token,
                acc_t_expiryDate,
                refresh_token,
                ref_t_expiryDate,
                role.getRoleName(),
                role.getPermissions()
        );
    }

    public static void sendToken(TokenResponse tokenResponse, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> data = new ObjectMapper().convertValue(tokenResponse,
                new TypeReference<>() {
                }
        );
        new ObjectMapper().writeValue(response.getOutputStream(), data);
    }
}
