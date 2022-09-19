package hamdam.bookee.tools.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnknownException(Exception exception) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                "Unknown error",
                exception.getMessage()
        );
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(
            ApiException exception,
            WebRequest webRequest
    ) {
        ApiResponse apiResponse = new ApiResponse(exception, webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError error = exception.getFieldError();
        String errorMessage;
        if (error != null) {
            errorMessage = "The field: {" + error.getField() + "} " + error.getDefaultMessage();
        } else {
            errorMessage = exception.getAllErrors().get(0).toString();
        }
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                "Bad Request",
                errorMessage
        );
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
