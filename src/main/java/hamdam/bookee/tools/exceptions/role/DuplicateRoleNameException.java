package hamdam.bookee.tools.exceptions.role;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicateRoleNameException extends ApiException {

    public DuplicateRoleNameException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
