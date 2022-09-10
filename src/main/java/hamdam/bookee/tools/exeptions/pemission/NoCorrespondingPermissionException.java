package hamdam.bookee.tools.exeptions.pemission;

import hamdam.bookee.tools.exeptions.ApiException;
import org.springframework.http.HttpStatus;

public class NoCorrespondingPermissionException extends ApiException {

    public NoCorrespondingPermissionException(String message){
        super(HttpStatus.FORBIDDEN, message);
    }
}
