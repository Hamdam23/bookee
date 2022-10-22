package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UnknownTokenException extends ApiException {

    public UnknownTokenException() {
        super(HttpStatus.BAD_REQUEST, "Error occurred on token!");
    }
}
