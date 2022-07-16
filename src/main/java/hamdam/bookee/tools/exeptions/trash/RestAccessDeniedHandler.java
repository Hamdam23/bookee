package hamdam.bookee.tools.exeptions.trash;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.tools.exeptions.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.FORBIDDEN,
                LocalDateTime.now(),
                "Access Denied",
                request.getRequestURL().toString());
        response.setStatus(403);

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, errorDetails);
        out.flush();
    }
}
