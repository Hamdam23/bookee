package hamdam.bookee.tools.exceptions.roleRequest;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

// TODO: 9/2/22 naming: InvalidRequestedRoleException vs InvalidRoleRequestException
public class NotAllowedRoleOnRequestException extends ApiException {
    public NotAllowedRoleOnRequestException() {
        super(
                HttpStatus.BAD_REQUEST,
                "Requested role only applies for [author] role"
        );
    }
}
