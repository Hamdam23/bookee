package hamdam.bookee.security;

import hamdam.bookee.filter.CustomAuthenticationFilter;
import hamdam.bookee.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
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
        //disabling working with cookies and sessions
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        //customising build-in "/login" URL to "/api/login"
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        //securing URLs
        /*permitAll - Specifies that all security roles are allowed to invoke the specified method(s)*/
        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**", "/api/v1/users/**", "/api/v1/images/**").permitAll();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
