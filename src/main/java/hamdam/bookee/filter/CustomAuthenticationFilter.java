package hamdam.bookee.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.auth.TokenResponse;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.token.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static hamdam.bookee.tools.constants.ConstantFields.USER_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    private final long millis = System.currentTimeMillis();

    private final Date acc_t_expiryDate = new Date(millis + 3600000); // 1 hour = 3600000
    private final Date ref_t_expiryDate = new Date(millis + 3600000 * 24 * 20); // 20 days

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
        log.info("Username is: {}", username);
        log.info("Password is: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // AuthenticationManager ga Authentication barish garak, va
        // UsernamePasswordAuthenticationToken Authentication ni implement qiladi
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserDetails user = (UserDetails) authResult.getPrincipal();
        TokenResponse tokenResponse = TokenProvider.generateToken(user, userRepository);
        TokenProvider.sendToken(tokenResponse, response);
//        log.error("user-user-details: ", user, ";");
//        String userName = user.getUsername();
//        log.error("user-app-user", userName, ";");
//
//        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//        String access_token = JWT.create()
//                .withSubject(user.getUsername())
//                .withExpiresAt(acc_t_expiryDate)
//                .withClaim(USER_NAME, userName)
//                .sign(algorithm);
//
//        Algorithm refAlgorithm = Algorithm.HMAC384("secret".getBytes());
//        String refresh_token = JWT.create()
//                .withSubject(user.getUsername())
//                .withExpiresAt(ref_t_expiryDate)
//                .withClaim(USER_NAME, userName)
//                .sign(refAlgorithm);
//
//        String role = userRepository.findAppUserByUserName(userName).get().getRole().getRoleName();
//
//        LinkedHashMap<String, Object> tokens = new LinkedHashMap<>();
//        tokens.put("access_token", access_token);
//        tokens.put("access token expires at", String.valueOf(acc_t_expiryDate));
//        tokens.put("refresh_token", refresh_token);
//        tokens.put("refresh token expires at", String.valueOf(ref_t_expiryDate));
//        tokens.put("role", role);
//        tokens.put("permissions", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
//        response.setContentType(APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
