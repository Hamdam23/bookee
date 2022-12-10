package hamdam.bookee.tools.exceptions.role;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NoDefaultRoleException extends ApiException {

    public NoDefaultRoleException() {
        super("There is no default role for users!", HttpStatus.NOT_IMPLEMENTED,
                "NoDefaultRoleException", new Object[]{});
    }
}
