package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UserTokenException extends ApiException {
    public UserTokenException() {
        super("The user of the token is invalid! Consider getting a new token!", HttpStatus.BAD_REQUEST,
                "UserTokenException", new Object[]{});
    }
}
