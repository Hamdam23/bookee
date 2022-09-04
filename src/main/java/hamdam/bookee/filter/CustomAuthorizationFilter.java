package hamdam.bookee.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.user.AppUser;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static hamdam.bookee.tools.constants.Endpoints.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

// TODO: 9/2/22 it is better not to use Custom prefix for naming, name must describe logic/implementation of filtering process
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    // TODO: 9/2/22 user service, not repository
    private final AppUserRepository userRepository;

    // TODO: 9/2/22 line length is too long, split it
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // TODO: 9/2/22 use shouldNotFilter() method to check if request is not for filtering
        if (request.getServletPath().equals(API_REGISTER) || request.getServletPath().equals(API_LOGIN) || request.getServletPath().equals(API_TOKEN_REFRESH)) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = TokenProvider.verifyToken(token, true);
                String username = decodedJWT.getSubject();
                Optional<AppUser> user = userRepository.findAppUserByUserName(username);
                // TODO: 9/2/22 handle get() call
                Set<Permissions> permissions = user.get().getRole().getPermissions();
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.name())));
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } catch (Exception exception) {
                // TODO: 9/2/22 don't write to response only error_message, write full ErrorResponse object
                log.error("Error logging in(token): {}", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }
}
