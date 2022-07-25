package hamdam.bookee.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilterConfigurer extends AbstractHttpConfigurer<AuthenticationFilterConfigurer, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/login");
        http.addFilter(authenticationFilter);
    }

    public static AuthenticationFilterConfigurer configureAuthenticationFilter() {
        return new AuthenticationFilterConfigurer();
    }
}
