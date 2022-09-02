package hamdam.bookee.filter;

import hamdam.bookee.APIs.auth.TokensResponse;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.token.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO: 9/2/22 do you really need to use this class? You are using username & pasword to login, in form data (like default implementation).
// TODO: 9/2/22 it is better not to use Custom prefix for naming, name must describe logic/implementation of filtering process
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }
    // TODO: 9/2/22 clear comments
//
//    private final long millis = System.currentTimeMillis();
//
//    private final Date acc_t_expiryDate = new Date(millis + 3600000); // 1 hour = 3600000
//    private final Date ref_t_expiryDate = new Date(millis + 3600000 * 24 * 20); // 20 days

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // TODO: 9/2/22 why in login process we use form data?
        // TODO: 9/2/22 why in login 'username', but in registration 'userName'?
        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
        // TODO: 9/2/22 logging password is not secure
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
        TokensResponse tokenResponse = TokenProvider.generateTokens(user, userRepository);
        TokenProvider.sendTokens(tokenResponse, response);
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
