package hamdam.bookee.tools.exceptions.roleRequest;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class AlreadyHasInProgressRequestException extends ApiException {

    public AlreadyHasInProgressRequestException() {
        super("You have existing IN_PROGRESS role request!", HttpStatus.FORBIDDEN,
                "AlreadyHasInProgressRequestException", new Object[]{});
    }
}
