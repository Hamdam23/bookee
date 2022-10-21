package hamdam.bookee.tools.exceptions.user;

import hamdam.bookee.tools.exceptions.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

public class UsernamePasswordWrongException extends ApiException {

    public UsernamePasswordWrongException(Type errorType) {
        super(HttpStatus.UNAUTHORIZED, String.format("%s is wrong", errorType.errorName));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum Type {
        USERNAME("username"),
        PASSWORD("password");

        private String errorName;
    }
}
