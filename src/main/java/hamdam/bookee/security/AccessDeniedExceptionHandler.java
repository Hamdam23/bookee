package hamdam.bookee.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.tools.exeptions.ApiException;
import hamdam.bookee.tools.exeptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

// TODO: 9/2/22 AccessDeniedHandler must be moved to security related package
// TODO: 9/2/22 naming is not clear, what is it for?
// TODO: 9/2/22 don't use My prefix for naming. Name must clearly describe the purpose of the class.
// TODO: 9/2/22 this todo applies for all code snippets in this project
@Component
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    // TODO: 9/2/22 write all method arguments in one line or each argument on separate line
    // TODO: 9/2/22 this todo applies for all code snippets in this project
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        // TODO: 9/2/22 return json response, not plain text
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        response.getWriter().write(mapper.writeValueAsString(new ErrorResponse(
                HttpStatus.FORBIDDEN, "Access denied", LocalDateTime.now())));
    }
}