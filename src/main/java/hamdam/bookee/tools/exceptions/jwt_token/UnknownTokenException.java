package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UnknownTokenException extends ApiException {

    public UnknownTokenException() {
        super("Error occurred on token!", HttpStatus.BAD_REQUEST,
                "UnknownTokenException", new Object[]{});
    }
}
