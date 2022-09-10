package hamdam.bookee.tools.exeptions.jwtToken;

import hamdam.bookee.tools.exeptions.ApiException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// TODO: 9/2/22 why there is ResponseStatus in some exceptions, but not in others?
// TODO: 9/2/22 do you really need it, why?
@Getter
public class RefreshTokenMissingException extends ApiException {

    // TODO: 9/2/22 do you really need message here, why?
    //private final String message;
    //  I do not even know why I need it

    public RefreshTokenMissingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
