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
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;

    public AuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }
    // TODO: 9/2/22 clear comments

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // TODO: 9/2/22 why in login process we use form data?
        // TODO: 9/2/22 why in login 'username', but in registration 'userName'?
        //  changed all to 'username'
        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
        // TODO: 9/2/22 logging password is not secure
        //  Sure it is insecure
        log.info("Username is: {}", username);
//        log.info("Password is: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserDetails user = (UserDetails) authResult.getPrincipal();
        TokensResponse tokensResponse = TokenProvider.getTokenResponse(user.getUsername(), userRepository);
        TokenProvider.displayToken(tokensResponse, response);
    }
}
