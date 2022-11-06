package hamdam.bookee.tools.exceptions.pemission;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class LimitedPermissionException extends ApiException {

    public LimitedPermissionException() {
        super("You do not have valid permission to access the operation!", HttpStatus.FORBIDDEN,
                "LimitedPermissionException", new Object[]{});
    }
}
