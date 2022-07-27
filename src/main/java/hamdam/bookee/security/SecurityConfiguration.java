package hamdam.bookee.security;

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

import static hamdam.bookee.filter.AuthenticationFilterConfigurer.configureAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MyAuthenticationEntryPoint entryPoint) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        //securing URLs
        // TODO only admins can set role to users not working.
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/users").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/users/set-role-to-user/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/users/set-image-to-user/**").hasAnyAuthority("ROLE_USER", "ROLE_AUTHOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("ROLE_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/roles").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/roles").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/roles/**").hasAuthority("ROLE_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/books").hasAnyAuthority("ROLE_AUTHOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/books/**").hasAnyAuthority("ROLE_AUTHOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/books").hasAnyAuthority("ROLE_USER", "ROLE_AUTHOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/books/**").hasAnyAuthority("ROLE_USER", "ROLE_AUTHOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAnyAuthority("ROLE_AUTHOR", "ROLE_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/genres").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/genres/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/genres").hasAnyAuthority("ROLE_USER", "ROLE_AUTHOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/genres/**").hasAnyAuthority("ROLE_USER", "ROLE_AUTHOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/genres/**").hasAuthority("ROLE_ADMIN");

        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh",
                        "/api/v1/images/**", "/api/v1/users/post").
                permitAll().anyRequest().authenticated().and().
                exceptionHandling().accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(entryPoint);
        http.apply(configureAuthenticationFilter());
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    MyAccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

//    @Bean
//    MyAuthenticationEntryPoint authenticationEntryPoint(AppUserRepository userRepository) {
//        return new MyAuthenticationEntryPoint(userRepository);
//    }
}
