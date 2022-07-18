package hamdam.bookee.security;

import hamdam.bookee.filter.CustomAuthenticationFilter;
import hamdam.bookee.filter.CustomAuthorizationFilter;
import hamdam.bookee.tools.exeptions.MyAccessDeniedHandler;
import hamdam.bookee.tools.exeptions.MyAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    /*UserDetailsService - faqat birgina
    * UserDetails loadUserByUsername(String username) method'iga ega interface.
    * Hozircha bu yerda auth'ga kerakli userDetailsService'ni beramiz va
    * loadUserByUsername method'ini AppUserService'da Override qilib config qilamiz.*/
    private final UserDetailsService userDetailsService;
    /*BCryptPasswordEncoder - bu shunchaki Password encoder ni extend qilgan class.
    * Va encode qilishni bir turi.*/
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //Authentication
    /*AuthenticationManagerBuilder - ?*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    //Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        //customising build-in "/login" URL to "/api/login"
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        //securing URLs
        // TODO only admins can set role to users not working.
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/users").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/users/set-role-to-user/**").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/users/set-image-to-user/**").hasAnyAuthority("APP_USER", "APP_AUTHOR", "APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority("APP_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("APP_ADMIN");

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
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
