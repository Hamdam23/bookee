package hamdam.bookee.tools.exceptions.role;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApiException {

    public DuplicateResourceException(String message){
        super(HttpStatus.BAD_REQUEST, "Duplicate " + message + " detected!");
    }
}
