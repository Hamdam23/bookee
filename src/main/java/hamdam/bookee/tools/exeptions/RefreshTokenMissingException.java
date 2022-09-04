package hamdam.bookee.tools.exeptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// TODO: 9/2/22 why there is ResponseStatus in some exceptions, but not in others?
// TODO: 9/2/22 do you really need it, why?
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class RefreshTokenMissingException extends ApiException {

    // TODO: 9/2/22 do you really need message here, why?
    private final String message;

    public RefreshTokenMissingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.message = message;
    }
}
