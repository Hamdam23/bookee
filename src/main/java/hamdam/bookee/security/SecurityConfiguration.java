package hamdam.bookee.security;

import hamdam.bookee.filter.CustomAuthorizationFilter;
import hamdam.bookee.tools.exeptions.MyAccessDeniedHandler;
import hamdam.bookee.tools.exeptions.MyAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static hamdam.bookee.filter.AuthenticationFilterConfigurer.configureAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        //securing URLs
        // TODO only admins can set role to users not working.
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/users").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/users/set-role-to-user/**").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/users/set-image-to-user/**").hasAnyAuthority("APP_USER", "APP_AUTHOR", "APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("APP_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/roles").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/roles").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/roles/**").hasAuthority("APP_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/books").hasAnyAuthority("APP_AUTHOR", "APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/books/**").hasAnyAuthority("APP_AUTHOR", "APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/books").hasAnyAuthority("APP_USER", "APP_AUTHOR", "APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/books/**").hasAnyAuthority("APP_USER", "APP_AUTHOR", "APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAnyAuthority("APP_AUTHOR", "APP_ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/genres").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/genres/**").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/genres").hasAnyAuthority("APP_USER", "APP_AUTHOR", "APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/genres/**").hasAnyAuthority("APP_USER", "APP_AUTHOR", "APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/genres/**").hasAuthority("APP_ADMIN");

        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**",
                        "/api/v1/images/**").
                permitAll().anyRequest().authenticated().
                and().
                exceptionHandling().accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint());
        http.apply(configureAuthenticationFilter());
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    MyAccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    MyAuthenticationEntryPoint authenticationEntryPoint() {
        return new MyAuthenticationEntryPoint();
    }
}
