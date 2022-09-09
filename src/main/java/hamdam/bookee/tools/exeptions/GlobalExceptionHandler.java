package hamdam.bookee.tools.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO: 9/2/22 method name is not clear, what is it for?
    //  Handling INTERNAL_SERVER_ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseSettings> handleInternalServerError(Exception exception) {
        ResponseSettings responseSettings = new ResponseSettings(
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                "Unknown error",
                exception.getMessage()
        );
        return new ResponseEntity<>(responseSettings, responseSettings.getStatus());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResponseSettings> handleApiException(
            ApiException exception,
            WebRequest webRequest
    ) {
        ResponseSettings responseSettings = new ResponseSettings(exception, webRequest.getDescription(false));
        return new ResponseEntity<>(responseSettings, responseSettings.getStatus());
    }
}
