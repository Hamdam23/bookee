package hamdam.bookee.tools.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

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
            WebRequest webRequest,
            Locale locale
    ) {
        ApiResponse apiResponse = new ApiResponse(
                exception.getStatus(),
                messageSource.getMessage(exception.getMessageId(), exception.getMessageArgs(), locale),
                webRequest.getDescription(false));
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
                "Bad Request!",
                errorMessage
        );
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                "Bad Request!",
                exception.getMessage()
        );
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                "File size exceeded!",
                "Maximum upload size exceeded; Configured maximum size is " + maxFileSize
        );
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
