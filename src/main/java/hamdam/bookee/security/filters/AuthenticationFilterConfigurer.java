package hamdam.bookee.security.filters;

import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static hamdam.bookee.tools.constants.Endpoints.API_LOGIN;

@Component
public class AuthenticationFilterConfigurer extends AbstractHttpConfigurer<AuthenticationFilterConfigurer, HttpSecurity> {
    private final AppUserService userService;
    private final HandlerExceptionResolver resolver;
    private final TokenUtils tokenUtils;

    public AuthenticationFilterConfigurer(
            AppUserService userService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
            TokenUtils tokenUtils) {
        this.userService = userService;
        this.resolver = resolver;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, userService, resolver, tokenUtils);
        authenticationFilter.setFilterProcessesUrl(API_LOGIN);
        http.addFilter(authenticationFilter);
    }
}
