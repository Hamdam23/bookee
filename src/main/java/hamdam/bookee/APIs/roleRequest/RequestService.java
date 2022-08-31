package hamdam.bookee.APIs.roleRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RequestService {

    RoleRequestResponse postRoleRequest(RequestRole requestRole, HttpServletRequest request);

    List<RoleRequestResponse> getAllRoleRequests(ReviewState reviewState, Long userId, HttpServletRequest request);

    RoleRequestResponse reviewRequest(Long id, ReviewState reviewState, HttpServletRequest request);
}
