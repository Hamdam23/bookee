package hamdam.bookee.tools.exeptions;

import org.springframework.http.HttpStatus;

public class InvalidRoleRequestException extends ApiException{
    public InvalidRoleRequestException(){
        super(
                HttpStatus.BAD_REQUEST,
                "Role request only applies for [user] role"
        );
    }
}
