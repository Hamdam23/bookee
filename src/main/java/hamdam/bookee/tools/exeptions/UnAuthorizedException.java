package hamdam.bookee.tools.exeptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
@Getter
public class UnAuthorizedException extends RuntimeException{
    private final String message;

    public UnAuthorizedException(String message) {
        this.message = message;
    }
}
