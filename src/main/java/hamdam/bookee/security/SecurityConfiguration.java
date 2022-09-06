package hamdam.bookee.security;

import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.filter.AuthenticationFilterConfigurer;
import hamdam.bookee.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static hamdam.bookee.tools.constants.Endpoints.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationEntryPointHandler entryPoint,
            AccessDeniedExceptionHandler accessDeniedHandler,
            AuthenticationFilterConfigurer authenticationFilterConfigurer,
            // TODO: 9/2/22 naming
            AuthorizationFilter authenticationFilter
    ) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        // TODO: 9/2/22 use constants for endpoints
        http.authorizeRequests().antMatchers(HttpMethod.POST, API_USER + "/post").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, API_USER + SET_IMAGE_TO_USER + "/**").fullyAuthenticated();

        http.authorizeRequests().antMatchers(HttpMethod.GET, API_BOOK + "/**").hasAuthority(Permissions.GET_BOOK.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, API_BOOK + "/**").hasAuthority(Permissions.CREATE_BOOK.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, API_BOOK + "/**").hasAuthority(Permissions.UPDATE_BOOK.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, API_BOOK + "/**").hasAuthority(Permissions.UPDATE_BOOK.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, API_BOOK + "/**").hasAuthority(Permissions.DELETE_BOOK.name());

        http.authorizeRequests().antMatchers(HttpMethod.GET, API_GENRE + "/**").hasAuthority(Permissions.GET_GENRE.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, API_GENRE + "/**").hasAuthority(Permissions.CREATE_GENRE.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, API_GENRE + "/**").hasAuthority(Permissions.UPDATE_GENRE.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, API_GENRE + "/**").hasAuthority(Permissions.UPDATE_GENRE.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, API_GENRE + "/**").hasAuthority(Permissions.DELETE_GENRE.name());

        http.authorizeRequests().antMatchers(HttpMethod.GET, API_USER + "/**").hasAuthority(Permissions.GET_USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, API_USER + "/**").hasAuthority(Permissions.CREATE_USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, API_USER + "/**").hasAuthority(Permissions.UPDATE_USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, API_USER + "/**").hasAuthority(Permissions.UPDATE_USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, API_USER + "/**").hasAuthority(Permissions.DELETE_USER.name());

        http.authorizeRequests().antMatchers(HttpMethod.GET, API_ROLE + "/**").hasAuthority(Permissions.GET_ROLE.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, API_ROLE + "/**").hasAuthority(Permissions.CREATE_ROLE.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, API_ROLE + "/**").hasAuthority(Permissions.UPDATE_ROLE.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, API_ROLE + "/**").hasAuthority(Permissions.UPDATE_ROLE.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, API_ROLE + "/**").hasAuthority(Permissions.DELETE_ROLE.name());

        http.authorizeRequests().antMatchers(API_REGISTER,
                        API_LOGIN + "/**",
                        API_TOKEN_REFRESH,
                        API_IMAGE + "/**").
                permitAll().anyRequest().authenticated().and().
                exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(entryPoint);
        http.apply(authenticationFilterConfigurer);
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
