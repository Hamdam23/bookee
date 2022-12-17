package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role_request.helpers.ReviewRequest;
import hamdam.bookee.APIs.role_request.helpers.RoleIdRoleRequest;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestResponseDTO;

import java.util.List;

public interface RoleRequestService {

    RoleRequestResponseDTO postRoleRequest(RoleIdRoleRequest roleIdRoleRequest);

    List<RoleRequestResponseDTO> getAllRoleRequests(State reviewState);

    RoleRequestResponseDTO reviewRequest(Long id, ReviewRequest reviewState);

    void deleteRequest(Long id);
}
