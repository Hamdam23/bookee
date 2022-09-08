package hamdam.bookee.tools.exeptions;

import org.springframework.http.HttpStatus;

public class NoCorrespondingPermissionException extends ApiException{

    public NoCorrespondingPermissionException(String message){
        super(HttpStatus.FORBIDDEN, message);
    }
}
