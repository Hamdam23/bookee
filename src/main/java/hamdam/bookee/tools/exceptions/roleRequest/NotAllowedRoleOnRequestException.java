package hamdam.bookee.tools.exceptions.roleRequest;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NotAllowedRoleOnRequestException extends ApiException {
    public NotAllowedRoleOnRequestException() {
        super("The requested role only applies to [author] role!", HttpStatus.BAD_REQUEST,
                "NotAllowedRoleOnRequestException", new Object[]{});
    }
}
