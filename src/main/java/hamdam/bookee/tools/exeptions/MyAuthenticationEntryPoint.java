package hamdam.bookee.tools.exeptions;

import hamdam.bookee.APIs.user.AppUserRepository;
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
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //TODO replace repo with service Status: DONE on 03.08.2022
    private final AppUserService userService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //TODO customise exception message DONE on 03.08.2022
        if (userService.invalidPassword(request.getParameter("username"))) {
            response.getWriter().write("Login failed: Invalid password!");
        } else {
            response.getWriter().write("Login failed: Invalid username!");
        }
    }
}
