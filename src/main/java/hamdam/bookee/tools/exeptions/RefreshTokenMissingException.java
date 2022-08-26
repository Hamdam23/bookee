package hamdam.bookee.tools.exeptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class RefreshTokenMissingException extends ApiException {

    private final String message;

    public RefreshTokenMissingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.message = message;
    }
}
