package hamdam.bookee.tools.exceptions.jwt_token;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class AlgorithmMismatchTokenException extends ApiException {

    public AlgorithmMismatchTokenException() {
        super(HttpStatus.BAD_REQUEST, "Algorithm of exception not supported!");
    }
}
