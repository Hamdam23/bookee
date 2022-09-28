package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role_request.helpers.ReviewStateDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;

import java.util.List;

// TODO: 9/2/22 why request object in service (logic) layer?
public interface RequestService {

    RoleRequestResponse postRoleRequest(RoleRequestDTO roleRequestDTO);

    List<RoleRequestResponse> getAllRoleRequests(State reviewState);

    RoleRequestResponse reviewRequest(Long id, ReviewStateDTO reviewState);

    void deleteRequest(Long id);
}