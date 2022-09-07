package hamdam.bookee.tools.exeptions.role;

import hamdam.bookee.tools.exeptions.ApiException;
import org.springframework.http.HttpStatus;

public class NoDefaultRoleException extends ApiException {

    public NoDefaultRoleException(String message) {
        super(HttpStatus.NOT_IMPLEMENTED, message);
    }
}
