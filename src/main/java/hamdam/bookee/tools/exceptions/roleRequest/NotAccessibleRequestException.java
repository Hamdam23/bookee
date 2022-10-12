package hamdam.bookee.tools.exceptions.roleRequest;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NotAccessibleRequestException extends ApiException {

    public NotAccessibleRequestException() {
        super(HttpStatus.FORBIDDEN, "You can not access the request!");
    }
}
