package hamdam.bookee.filter;

import hamdam.bookee.APIs.auth.TokensResponse;
import hamdam.bookee.APIs.user.AppUser;
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
import java.util.Date;
import java.util.Optional;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
        log.info("Username is: {}", username);
        log.info("Password is: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserDetails user = (UserDetails) authResult.getPrincipal();
        TokensResponse tokenResponse = TokenProvider.generateTokens(user, userRepository);
        TokenProvider.sendTokens(tokenResponse, response);
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
