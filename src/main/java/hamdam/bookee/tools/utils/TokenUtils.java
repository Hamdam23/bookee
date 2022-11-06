package hamdam.bookee.tools.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.auth.TokensResponse;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.tools.exceptions.jwt_token.MissingTokenException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class TokenUtils implements InitializingBean {

    private final String SECRET;
    private static long accessTokenValidity;
    private static long refreshTokenValidity;

    private static Algorithm accessTokenAlgorithm;
    private static Algorithm refreshTokenAlgorithm;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public TokenUtils(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.access-token-validity}") long accessTokenValidity,
                      @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
        this.SECRET = secret;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    @Override
    public void afterPropertiesSet() {
        accessTokenAlgorithm = Algorithm.HMAC256(SECRET.getBytes());
        refreshTokenAlgorithm = Algorithm.HMAC384(SECRET.getBytes());
    }

    public static DecodedJWT decodeToken(String token, boolean isAccessToken) {
        JWTVerifier verifier;
        if (isAccessToken) {
            verifier = JWT.require(accessTokenAlgorithm).build();
        } else {
            verifier = JWT.require(refreshTokenAlgorithm).build();
        }
        return verifier.verify(token);
    }

    public static String getUsernameFromToken(String header, boolean isAccessToken) {
        String token = header.substring("Bearer ".length());
        DecodedJWT decodedJWT = TokenUtils.decodeToken(token, isAccessToken);
        return decodedJWT.getSubject();
    }

    public static TokensResponse getTokenResponse(AppUserEntity user) {

        return new TokensResponse(
                createToken(user.getUsername(), user.getRole(), true),
                DATE_FORMAT.format(new Date(System.currentTimeMillis() + accessTokenValidity)),
                createToken(user.getUsername(), user.getRole(), false),
                DATE_FORMAT.format(new Date(System.currentTimeMillis() + refreshTokenValidity)),
                user.getRole().getRoleName(),
                user.getRole().getPermissions()
        );
    }

    public static TokensResponse getAccessTokenResponse(AppUserEntity user) {

        return new TokensResponse(
                createToken(user.getUsername(), user.getRole(), true),
                DATE_FORMAT.format(new Date(System.currentTimeMillis() + accessTokenValidity))
        );
    }

    public static String createToken(String username, AppRoleEntity role, boolean isAccessToken) {

        if (isAccessToken) {
            return JWT.create()
                    .withSubject(username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidity))
                    .withClaim("role", role.getRoleName())
                    .sign(accessTokenAlgorithm);
        } else {
            return JWT.create()
                    .withSubject(username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidity))
                    .withClaim("role", role.getRoleName())
                    .sign(refreshTokenAlgorithm);
        }
    }

    public static void sendTokenInBody(
            TokensResponse tokensResponse,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> data = new ObjectMapper().convertValue(tokensResponse,
                new TypeReference<>() {
                }
        );
        new ObjectMapper().writeValue(response.getOutputStream(), data);
    }

    public static void checkHeader(String header, boolean isAccessToken) {
        if (header == null || !header.startsWith("Bearer ")) {
            if (isAccessToken) throw new MissingTokenException("Access");
            throw new MissingTokenException("Refresh");
        }
    }
}
