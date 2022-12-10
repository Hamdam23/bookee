package hamdam.bookee.security.filters;

import hamdam.bookee.APIs.auth.TokensResponse;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.exceptions.user.UsernamePasswordWrongException;
import hamdam.bookee.tools.utils.TokenProvider;
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

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AppUserService userService;
    private final HandlerExceptionResolver resolver;
    private final TokenProvider tokenProvider;

    //It takes the username and password from the request
    // and creates a new UsernamePasswordAuthenticationToken object
    // TODO you can use default implementation of attemptAuthentication method in UsernamePasswordAuthenticationFilter
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // TODO: 9/2/22 why in login process we use form data?
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    // If the user exists with username, the password is wrong.
    // Else username is wrong
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        UsernamePasswordWrongException.Type type;
        if (userService.existsWithUsername(obtainUsername(request))) {
            type = UsernamePasswordWrongException.Type.PASSWORD;
        } else {
            type = UsernamePasswordWrongException.Type.USERNAME;
        }
        resolver.resolveException(request, response, null, new UsernamePasswordWrongException(type));
    }

    // If the authentication was successful, send the tokens in the response body.
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {
        UserDetails user = (UserDetails) authResult.getPrincipal();
        TokensResponse tokensResponse = tokenProvider.getTokenResponse(userService.getUserByUsername(user.getUsername(), true));
        tokenProvider.sendTokenInBody(tokensResponse, response);
    }
}
