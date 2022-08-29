package hamdam.bookee.APIs.roleRequest;

import javax.servlet.http.HttpServletRequest;

public interface RequestService {

    RoleRequestResponse postRoleRequest(RequestRole requestRole, HttpServletRequest request);
}
