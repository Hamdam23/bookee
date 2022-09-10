package hamdam.bookee.security.filter;

import hamdam.bookee.APIs.user.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

import static hamdam.bookee.tools.constants.Endpoints.API_LOGIN;

@Component
public class AuthenticationFilterConfigurer extends AbstractHttpConfigurer<AuthenticationFilterConfigurer, HttpSecurity> {
    private final AppUserRepository userRepository;

    public AuthenticationFilterConfigurer(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, userRepository);
        authenticationFilter.setFilterProcessesUrl(API_LOGIN);
        http.addFilter(authenticationFilter);
    }
}
