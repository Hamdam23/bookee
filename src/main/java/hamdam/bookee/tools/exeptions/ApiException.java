package hamdam.bookee.tools.exeptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ApiException extends RuntimeException {

    private HttpStatus status;
    // TODO: 9/2/22 do you really need JsonFormat here, why?
    //  No I do not need it.
    private String message;
}
