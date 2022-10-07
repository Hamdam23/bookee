package hamdam.bookee.security.handlers;

import hamdam.bookee.APIs.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final AppUserService userService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // TODO: 9/2/22 return json response, not plain text
        if (userService.userExistsWithUsername(request.getParameter("username"))) {
            response.getWriter().write("Login failed: Invalid password!");
        } else {
            response.getWriter().write("Login failed: Invalid username!");
        }
    }
}
