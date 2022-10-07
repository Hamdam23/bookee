package hamdam.bookee.tools.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApiException {

    public DuplicateResourceException(String message) {
        super(HttpStatus.BAD_REQUEST, "Duplicate " + message + " detected!");
    }

    public DuplicateResourceException(String fieldName, String resourceType, String fieldValue) {
        super(HttpStatus.BAD_REQUEST, String.format("Duplicate %s detected on %s: [%s]", fieldName, resourceType, fieldValue));
    }
}
