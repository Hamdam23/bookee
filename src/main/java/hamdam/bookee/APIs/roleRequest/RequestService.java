package hamdam.bookee.APIs.roleRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RequestService {

    RoleRequestResponse postRoleRequest(RequestRole requestRole, HttpServletRequest request);

    List<RoleRequestResponse> getAllRoleRequests(ReviewState reviewState, HttpServletRequest request);

    RoleRequestResponse reviewRequest(Long id, ReviewState reviewState, HttpServletRequest request);

    void deleteRequest(Long id, HttpServletRequest request);
}
