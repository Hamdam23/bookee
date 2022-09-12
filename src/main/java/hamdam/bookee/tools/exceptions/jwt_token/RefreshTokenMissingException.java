package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RefreshTokenMissingException extends ApiException {

    public RefreshTokenMissingException() {
        super(HttpStatus.BAD_REQUEST, "Refresh token is missing!");
    }
}
