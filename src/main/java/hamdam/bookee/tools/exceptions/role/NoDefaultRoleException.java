package hamdam.bookee.tools.exceptions.role;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NoDefaultRoleException extends ApiException {

    public NoDefaultRoleException(String message) {
        super(HttpStatus.NOT_IMPLEMENTED, message);
    }
}
