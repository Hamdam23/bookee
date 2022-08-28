package hamdam.bookee.tools.exeptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String details;

    public ErrorResponse(ApiException apiException) {
        this.status = apiException.getStatus();
        this.message = apiException.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(ApiException apiException, String details) {
        this.status = apiException.getStatus();
        this.message = apiException.getMessage();
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
