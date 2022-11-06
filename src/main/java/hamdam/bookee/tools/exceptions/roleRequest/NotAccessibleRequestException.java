package hamdam.bookee.tools.exceptions.roleRequest;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NotAccessibleRequestException extends ApiException {

    public NotAccessibleRequestException() {
        super("You can not access the request!", HttpStatus.FORBIDDEN,
                "NotAccessibleRequestException", new Object[]{});
    }
}
