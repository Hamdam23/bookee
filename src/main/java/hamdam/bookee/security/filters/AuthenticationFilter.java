package hamdam.bookee.security.filters;

import hamdam.bookee.APIs.auth.TokensResponse;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.exceptions.user.UsernamePasswordWrongException;
import hamdam.bookee.tools.token.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO: 9/2/22 do you really need to use this class? You are using username & password to login, in form data (like default implementation).
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AppUserService userService;
    private final HandlerExceptionResolver resolver;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // TODO: 9/2/22 why in login process we use form data?
        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        UsernamePasswordWrongException.Type type;
        if (userService.existsWithUsername(request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY))) {
            type = UsernamePasswordWrongException.Type.PASSWORD;
        } else {
            type = UsernamePasswordWrongException.Type.USERNAME;
        }
        resolver.resolveException(request, response, null, new UsernamePasswordWrongException(type));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {
        UserDetails user = (UserDetails) authResult.getPrincipal();
        TokensResponse tokensResponse = TokenUtils.getTokenResponse(userService.getUserByUsername(user.getUsername()));
        TokenUtils.presentToken(tokensResponse, response);
    }
}
