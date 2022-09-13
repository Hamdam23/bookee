package hamdam.bookee.tools.exceptions.roleRequest;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class IncorrectStateValueException extends ApiException {

    public IncorrectStateValueException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
