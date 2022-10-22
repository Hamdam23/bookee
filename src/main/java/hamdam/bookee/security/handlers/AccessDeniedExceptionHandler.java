package hamdam.bookee.security.handlers;

import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: 9/2/22 naming is not clear, what is it for?
@Component
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    public AccessDeniedExceptionHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) {
        resolver.resolveException(request, response, null, new LimitedPermissionException());
    }
}