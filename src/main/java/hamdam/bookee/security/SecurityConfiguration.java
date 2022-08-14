package hamdam.bookee.security;

import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.filter.AuthenticationFilterConfigurer;
import hamdam.bookee.filter.CustomAuthorizationFilter;
import hamdam.bookee.tools.exeptions.MyAccessDeniedHandler;
import hamdam.bookee.tools.exeptions.MyAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static hamdam.bookee.tools.constants.Endpoints.API_TOKEN_REFRESH;
import static hamdam.bookee.tools.constants.Endpoints.API_REGISTER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            MyAuthenticationEntryPoint entryPoint,
            MyAccessDeniedHandler accessDeniedHandler,
            AuthenticationFilterConfigurer authenticationFilterConfigurer,
            CustomAuthorizationFilter authenticationFilter
    ) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/users/post").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/users/set-image-to-user/**").fullyAuthenticated();

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/books/**").hasAuthority(Permissions.GET_BOOK.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/books/**").hasAuthority(Permissions.CREATE_BOOK.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/books/**").hasAuthority(Permissions.UPDATE_BOOK.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/books/**").hasAuthority(Permissions.UPDATE_BOOK.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAuthority(Permissions.DELETE_BOOK.name());

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/genres/**").hasAuthority(Permissions.GET_GENRE.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/genres/**").hasAuthority(Permissions.CREATE_GENRE.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/genres/**").hasAuthority(Permissions.UPDATE_GENRE.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/genres/**").hasAuthority(Permissions.UPDATE_GENRE.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/genres/**").hasAuthority(Permissions.DELETE_GENRE.name());

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority(Permissions.GET_USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/users/**").hasAuthority(Permissions.CREATE_USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAuthority(Permissions.UPDATE_USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasAuthority(Permissions.UPDATE_USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority(Permissions.DELETE_USER.name());

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/roles/**").hasAuthority(Permissions.GET_ROLE.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/roles/**").hasAuthority(Permissions.CREATE_ROLE.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/roles/**").hasAuthority(Permissions.UPDATE_ROLE.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/roles/**").hasAuthority(Permissions.UPDATE_ROLE.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/roles/**").hasAuthority(Permissions.DELETE_ROLE.name());

        http.authorizeRequests().antMatchers(API_REGISTER, "/api/login/**", API_TOKEN_REFRESH,
                        "/api/v1/images/**").
                permitAll().anyRequest().authenticated().and().
                exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(entryPoint);
        http.apply(authenticationFilterConfigurer);
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
