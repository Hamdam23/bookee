package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class SignatureTokenException extends ApiException {

    public SignatureTokenException() {
        super("Check the signature of the given token!", HttpStatus.BAD_REQUEST,
                "SignatureTokenException", new Object[]{});
    }
}
