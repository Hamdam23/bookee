/**
 * This class is responsible for handling all requests related to role requests
 */
package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.role_request.helpers.ReviewRequestDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import hamdam.bookee.tools.exceptions.roleRequest.AlreadyHasInProgressRequestException;
import hamdam.bookee.tools.exceptions.roleRequest.IncorrectStateValueException;
import hamdam.bookee.tools.exceptions.roleRequest.NotAccessibleRequestException;
import hamdam.bookee.tools.exceptions.roleRequest.NotAllowedRoleOnRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.CREATE_ROLE_REQUEST;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE_REQUEST;
import static hamdam.bookee.APIs.role_request.State.*;
import static hamdam.bookee.tools.utils.SecurityUtils.getUserByRequest;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final AppRoleRepository roleRepository;
    private final AppUserRepository userRepository;

    @Override
    public RoleRequestResponse postRoleRequest(RoleRequestDTO roleRequestDTO) {
        AppUserEntity requestingUser = getUserByRequest(userRepository);
        if (requestRepository.existsByUserAndState(requestingUser, IN_PROGRESS)) {
            throw new AlreadyHasInProgressRequestException();
        }
        AppRoleEntity role = roleRepository.findById(roleRequestDTO.getRoleId()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleRequestDTO.getRoleId()));
        Set<Permissions> permissionsSet = getUserPermissions(requestingUser);

        if (!permissionsSet.contains(CREATE_ROLE_REQUEST)) {
            throw new LimitedPermissionException();
        } else if (role.getPermissions().contains(MONITOR_ROLE_REQUEST)) {
            throw new NotAllowedRoleOnRequestException();
        }
        RequestEntity requestEntity =
                requestRepository.save(new RequestEntity(requestingUser, role, State.IN_PROGRESS));

        return new RoleRequestResponse(requestEntity, role.getRoleName());
    }

    @Override
    public List<RoleRequestResponse> getAllRoleRequests(State state) {
        List<RequestEntity> responseList;
        if (state == null) {
            responseList = requestRepository.findAll();
        } else {
            responseList = requestRepository.findAllByState(state);
        }

        AppUserEntity requestingUser = getUserByRequest(userRepository);
        Set<Permissions> permissionsSet = getUserPermissions(requestingUser);
        if (!permissionsSet.contains(MONITOR_ROLE_REQUEST)) {
            responseList = requestRepository.findAllByUser(requestingUser);
        }
        List<RoleRequestResponse> requestResponses = new ArrayList<>();
        responseList.forEach(response -> {
            RoleRequestResponse requestResponse = new RoleRequestResponse(response, response.getRequestedRole().getRoleName());
            requestResponses.add(requestResponse);
        });
        return requestResponses;
    }

    /**
     * > This function is used to review a role request
     *
     * @param id The id of the request to be reviewed
     * @param review The review object that contains the state and description of the review.
     * @return A RoleRequestResponse object is being returned.
     */
    @Override
    public RoleRequestResponse reviewRequest(Long id, ReviewRequestDTO review) {
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role request", "id", id)
        );
        AppUserEntity requestingUser = getUserByRequest(userRepository);
        Set<Permissions> permissionsSet = getUserPermissions(requestingUser);

        // This is checking if the user has the permission to monitor role requests.
        // If they do not, then they are not allowed to review a request.
        if (!permissionsSet.contains(MONITOR_ROLE_REQUEST)) {
            throw new LimitedPermissionException();
        } else if (!review.getState().equals(ACCEPTED) && !review.getState().equals(DECLINED)) {
            throw new IncorrectStateValueException();
        }

        if (review.getDescription() != null) {
            requestEntity.setDescription(review.getDescription());
        }

        AppUserEntity user = requestEntity.getUser();
        if (review.getState().equals(ACCEPTED)) {
            user.setRole(requestEntity.getRequestedRole());
            userRepository.save(user);
        }

        requestEntity.setState(review.getState());
        requestRepository.save(requestEntity);

        return new RoleRequestResponse(requestEntity, requestEntity.getRequestedRole().getRoleName());
    }

    @Override
    public void deleteRequest(Long id) {
        RequestEntity requestEntity = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role request", "id", id)
                );

        AppUserEntity requestingUser = getUserByRequest(userRepository);
        Set<Permissions> permissionsSet = getUserPermissions(requestingUser);
        if (permissionsSet.contains(MONITOR_ROLE_REQUEST)) {
            requestRepository.deleteById(id);
        } else {
            if (roleRequestBelongsUser(requestingUser, requestEntity)) {
                requestRepository.deleteById(id);
            } else {
                throw new NotAccessibleRequestException();
            }
        }

        requestRepository.save(requestEntity);
    }

    boolean roleRequestBelongsUser(AppUserEntity user, RequestEntity requestEntity) {
        return Objects.equals(requestEntity.getUser().getId(), user.getId());
    }

    /**
     * Get the permissions of a user by getting the permissions of the role of the user.
     *
     * @param user The user object that is being authenticated.
     * @return A set of permissions.
     */
    public Set<Permissions> getUserPermissions(AppUserEntity user) {
        return user.getRole().getPermissions();
    }
}
