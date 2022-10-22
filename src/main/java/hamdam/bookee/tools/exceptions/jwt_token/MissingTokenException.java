package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class MissingTokenException extends ApiException {

    public MissingTokenException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
