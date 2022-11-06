package hamdam.bookee.tools.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiException extends RuntimeException {

    private HttpStatus status;
    private String messageId;
    private Object[] messageArgs;

    public ApiException(String message, HttpStatus status, String messageId, Object[] messageArgs) {
        super(message);
        this.status = status;
        this.messageId = messageId;
        this.messageArgs = messageArgs;
    }
}
