package hamdam.bookee.tools.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApiException {

    public DuplicateResourceException(String message) {
        super(HttpStatus.BAD_REQUEST, "Duplicate " + message + " detected!");
    }

    public DuplicateResourceException(String message, String value1, String value2) {
        super(HttpStatus.BAD_REQUEST, String.format("Duplicate %s detected on %s and [%s]", message, value1, value2));
    }
}
