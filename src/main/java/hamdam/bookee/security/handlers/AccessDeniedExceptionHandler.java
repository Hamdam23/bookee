package hamdam.bookee.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.tools.exceptions.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

// TODO: 9/2/22 naming is not clear, what is it for?
@Component
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        // TODO: 9/2/22 return json response, not plain text
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        response.getWriter().write(objectMapper.writeValueAsString(new ApiResponse(
                HttpStatus.FORBIDDEN, LocalDateTime.now(), "Access denied")));
    }
}