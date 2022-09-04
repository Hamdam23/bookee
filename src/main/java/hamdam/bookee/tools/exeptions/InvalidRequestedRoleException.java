package hamdam.bookee.tools.exeptions;

import org.springframework.http.HttpStatus;

// TODO: 9/2/22 naming: InvalidRequestedRoleException vs InvalidRoleRequestException
public class InvalidRequestedRoleException extends ApiException {
    public InvalidRequestedRoleException() {
        super(
                HttpStatus.BAD_REQUEST,
                "Requested role only applies for [author] role"
        );
    }
}
