package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UserTokenException extends ApiException {
    public UserTokenException() {
        super(HttpStatus.BAD_REQUEST, "User of the token is invalid! Consider getting new token.");
    }
}
