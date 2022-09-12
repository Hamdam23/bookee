package hamdam.bookee.tools.exceptions.pemission;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NoCorrespondingPermissionException extends ApiException {

    public NoCorrespondingPermissionException(String message){
        super(HttpStatus.FORBIDDEN, message);
    }
}
