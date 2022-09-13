package hamdam.bookee.tools.exceptions.pemission;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class LimitedPermissionException extends ApiException {

    public LimitedPermissionException(String message){
        super(HttpStatus.FORBIDDEN, message);
    }
}
