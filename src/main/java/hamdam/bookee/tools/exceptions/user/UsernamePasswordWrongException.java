package hamdam.bookee.tools.exceptions.user;

import hamdam.bookee.tools.exceptions.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

public class UsernamePasswordWrongException extends ApiException {

    public UsernamePasswordWrongException(Type errorType) {
        super(String.format("%s is wrong", errorType.errorName), HttpStatus.UNAUTHORIZED,
                "UsernamePasswordWrongException", new Object[]{errorType.errorName});
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
