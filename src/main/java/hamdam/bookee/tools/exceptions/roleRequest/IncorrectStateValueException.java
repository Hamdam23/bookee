package hamdam.bookee.tools.exceptions.roleRequest;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class IncorrectStateValueException extends ApiException {

    public IncorrectStateValueException() {
        super("State can be either ACCEPTED or DECLINED", HttpStatus.BAD_REQUEST,
                "IncorrectStateValueException", new Object[]{});
    }
}
