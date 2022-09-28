package hamdam.bookee.tools.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ApiException extends RuntimeException {

    private HttpStatus status;
    private String message;
}
