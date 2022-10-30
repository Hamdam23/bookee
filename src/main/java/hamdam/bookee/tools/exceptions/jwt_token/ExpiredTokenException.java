package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends ApiException {

    public ExpiredTokenException() {
        super("Token is expired!", HttpStatus.BAD_REQUEST,
                "ExpiredTokenException", new Object[]{});
    }
}
