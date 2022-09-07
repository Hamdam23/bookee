package hamdam.bookee.tools.exeptions.role;

import hamdam.bookee.tools.exeptions.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicateRoleNameException extends ApiException {

    public DuplicateRoleNameException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
