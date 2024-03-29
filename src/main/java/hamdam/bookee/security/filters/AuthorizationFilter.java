package hamdam.bookee.security.filters;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.jwt_token.*;
import hamdam.bookee.tools.utils.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static hamdam.bookee.tools.constants.Endpoints.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final AppUserService appUserService;
    private final HandlerExceptionResolver resolver;
    private final TokenProvider tokenProvider;

    public AuthorizationFilter(AppUserService appUserService,
                               @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, TokenProvider tokenProvider) {
        this.appUserService = appUserService;
        this.resolver = resolver;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals(API_REGISTER) ||
                path.equals(API_LOGIN) ||
                path.equals(API_TOKEN_REFRESH);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        try {
            String header = request.getHeader(AUTHORIZATION);
            tokenProvider.checkHeader(header, true);
            String username = tokenProvider.getUsernameFromToken(header, true);
            AppUserEntity user = appUserService.getUserByUsername(username, true);
            Set<Permissions> permissions = user.getRole().getPermissions();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            // TODO use mapper for authorities&permission
            permissions.forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.name())));

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (MissingTokenException exception) {
            resolver.resolveException(request, response, null, exception);
        } catch (ResourceNotFoundException exception) {
            resolver.resolveException(request, response, null, new UserTokenException());
        } catch (AlgorithmMismatchException exception) {
            resolver.resolveException(request, response, null, new AlgorithmMismatchTokenException());
        } catch (SignatureVerificationException exception) {
            resolver.resolveException(request, response, null, new SignatureTokenException());
        } catch (TokenExpiredException exception) {
            resolver.resolveException(request, response, null, new ExpiredTokenException());
        } catch (Exception exception) {
            resolver.resolveException(request, response, null, new UnknownTokenException());
        }
    }
}
