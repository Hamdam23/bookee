package hamdam.bookee.filter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.user.AppUser;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.exeptions.RefreshTokenMissingException;
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
public class AuthorizationFilter extends OncePerRequestFilter {
    // TODO: 9/2/22 user service, not repository
    private final AppUserService appUserService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals(API_REGISTER) ||
                path.equals(API_LOGIN) ||
                path.equals(API_TOKEN_REFRESH);
    }

    // TODO: 9/2/22 line length is too long, split it
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RefreshTokenMissingException("No JWT token found in request headers");
        }
        try {
            String token = header.substring("Bearer ".length());
            DecodedJWT decodedJWT = TokenProvider.decodeToken(token, true);
            String username = decodedJWT.getSubject();
            AppUser user = appUserService.getUserByUsername(username);
            // TODO: 9/2/22 handle get() call
            Set<Permissions> permissions = user.getRole().getPermissions();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.name())));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (SignatureVerificationException exception) {
            // TODO: 9/2/22 don't write to response only error_message, write full ErrorResponse object
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("message", exception.getMessage());
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
//        }
    }
}
