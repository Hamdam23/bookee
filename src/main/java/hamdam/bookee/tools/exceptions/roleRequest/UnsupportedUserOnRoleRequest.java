package hamdam.bookee.tools.exceptions.roleRequest;

import hamdam.bookee.tools.exceptions.ApiException;
import org.springframework.http.HttpStatus;

// TODO: 9/2/22 naming: InvalidRequestedRoleException vs InvalidRoleRequestException
public class UnsupportedUserOnRoleRequest extends ApiException {
    public UnsupportedUserOnRoleRequest(){
        super(
                HttpStatus.BAD_REQUEST,
                "Role request only applies for [user] role"
        );
    }
}