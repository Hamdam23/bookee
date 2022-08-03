package hamdam.bookee.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static hamdam.bookee.tools.constants.Endpoints.API_LOGIN;

public class AuthenticationFilterConfigurer extends AbstractHttpConfigurer<AuthenticationFilterConfigurer, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl(API_LOGIN);
        http.addFilter(authenticationFilter);
    }

    public static AuthenticationFilterConfigurer configureAuthenticationFilter() {
        return new AuthenticationFilterConfigurer();
    }
}
