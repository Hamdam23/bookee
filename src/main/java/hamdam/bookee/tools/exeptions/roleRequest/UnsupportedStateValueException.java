package hamdam.bookee.tools.exeptions.roleRequest;

import hamdam.bookee.tools.exeptions.ApiException;
import org.springframework.http.HttpStatus;

public class UnsupportedStateValueException extends ApiException {

    public UnsupportedStateValueException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
