package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role_request.helpers.ReviewRequestDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;

import java.util.List;

public interface RoleRequestService {

    RoleRequestResponse postRoleRequest(RoleRequestDTO roleRequestDTO);

    List<RoleRequestResponse> getAllRoleRequests(State reviewState);

    RoleRequestResponse reviewRequest(Long id, ReviewRequestDTO reviewState);

    void deleteRequest(Long id);
}
