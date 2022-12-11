package hamdam.bookee.tools.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnknownException(Exception exception, Locale locale) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                messageSource.getMessage("Exception", null, locale),
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
        String message;
        try {
            message = messageSource.getMessage(exception.getMessageId(), exception.getMessageArgs(), locale);
        } catch (NoSuchMessageException e) {
            message = exception.getMessage();
        }
        ApiResponse apiResponse = new ApiResponse(
                exception.getStatus(),
                message,
                webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, Locale locale) {
        FieldError error = exception.getFieldError();
        String errorMessage;
        if (error != null) {
            errorMessage = exception.getMessage();
        } else {
            errorMessage = exception.getAllErrors().get(0).toString();
        }
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                messageSource.getMessage("MethodArgumentNotValidException", null, locale),
                errorMessage
        );
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException exception, Locale locale) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                messageSource.getMessage("ConstraintViolationException", null, locale),
                exception.getMessage()
        );
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception, Locale locale) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                messageSource.getMessage("MaxUploadSizeExceededException", null, locale),
                exception.getMessage()
        );
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
