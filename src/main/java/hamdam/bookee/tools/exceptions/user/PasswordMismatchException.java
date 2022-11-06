package hamdam.bookee.tools.exceptions.user;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends ApiException {

    public PasswordMismatchException(String firstPassword, String secondPassword) {
        super(String.format("%s does not match with %s!", firstPassword, secondPassword), HttpStatus.BAD_REQUEST,
                "PasswordMismatchException", new Object[]{firstPassword, secondPassword});
    }
}
