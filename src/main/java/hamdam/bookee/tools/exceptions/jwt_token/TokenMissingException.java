package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TokenMissingException extends ApiException {

    public TokenMissingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
