package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class MissingTokenException extends ApiException {

    public MissingTokenException(String message) {
        super(message + " token is missing!", HttpStatus.BAD_REQUEST,
                "MissingTokenException", new Object[]{message});
    }
}
