package hamdam.bookee.tools.exeptions.jwtToken;

import hamdam.bookee.tools.exeptions.ApiException;
import org.springframework.http.HttpStatus;

public class JWTDecodeExceptionHandler extends ApiException {

    public JWTDecodeExceptionHandler(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
