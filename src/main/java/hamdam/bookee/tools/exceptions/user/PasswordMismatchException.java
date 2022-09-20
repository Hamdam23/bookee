package hamdam.bookee.tools.exceptions.user;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends ApiException {

    public PasswordMismatchException(String value1, String value2) {
        super(HttpStatus.BAD_REQUEST, String.format("%s does not match with &s!", value1, value2 + "!"));
    }
}
