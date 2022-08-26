package hamdam.bookee.tools.exeptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class RefreshTokenMissingException extends ErrorDetails {

    private final String message;

    public RefreshTokenMissingException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                message
        );
        this.message = message;
    }
}
