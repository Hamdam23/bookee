package hamdam.bookee.tools.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApiException {

    public DuplicateResourceException(String fieldName) {
        super("Duplicate " + fieldName + " detected!", HttpStatus.BAD_REQUEST,
                "DuplicateResourceExceptionOneArg", new Object[]{fieldName});
    }

    public DuplicateResourceException(String fieldName, String resourceType, String fieldValue) {
        super(String.format("Duplicate %s detected on %s: [%s]", fieldName, resourceType, fieldValue), HttpStatus.BAD_REQUEST,
                "DuplicateResourceExceptionMoreArgs", new Object[]{fieldName, resourceType, fieldValue});
    }
}
