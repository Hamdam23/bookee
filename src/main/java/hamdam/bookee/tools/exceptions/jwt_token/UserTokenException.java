package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UserTokenException extends ApiException {
    public UserTokenException() {
        super("User of the token is invalid! Consider getting new token", HttpStatus.BAD_REQUEST,
                "UserTokenException", new Object[]{});
    }
}
