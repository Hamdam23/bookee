package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.role_request.helpers.ReviewStateDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import hamdam.bookee.tools.exceptions.roleRequest.AlreadyHasInProgressRequestException;
import hamdam.bookee.tools.exceptions.roleRequest.NotAllowedRoleOnRequestException;
import hamdam.bookee.tools.exceptions.roleRequest.IncorrectStateValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.CREATE_ROLE_REQUEST;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE_REQUEST;
import static hamdam.bookee.APIs.role_request.State.*;
import static hamdam.bookee.tools.token.GetUserByToken.getUserByRequest;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final AppRoleRepository roleRepository;
    private final AppUserRepository userRepository;

    @Override
    public RoleRequestResponse postRoleRequest(RoleRequestDTO roleRequestDTO) {
        AppUserEntity currentUser = getUserByRequest(userRepository);
        if (requestRepository.existsByUserAndState(currentUser, IN_PROGRESS)) {
            throw new AlreadyHasInProgressRequestException();
        }
        AppRoleEntity role = roleRepository.findById(roleRequestDTO.getRoleId()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleRequestDTO.getRoleId()));
        Set<Permissions> permissionsSet = getUserPermissions(currentUser);

        if (!permissionsSet.contains(CREATE_ROLE_REQUEST)) {
            throw new LimitedPermissionException();
        } else if (role.getPermissions().contains(MONITOR_ROLE_REQUEST)) {
            throw new NotAllowedRoleOnRequestException();
        }
        RequestEntity requestEntity = new RequestEntity(currentUser, role, State.IN_PROGRESS);
        requestRepository.save(requestEntity);

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

        AppUserEntity appUserEntity = getUserByRequest(userRepository);
        Set<Permissions> permissionsSet = getUserPermissions(appUserEntity);
        if (!permissionsSet.contains(MONITOR_ROLE_REQUEST)) {
            responseList = requestRepository.findAllByUser(appUserEntity);
        }
        List<RoleRequestResponse> requestResponses = new ArrayList<>();
        responseList.forEach(response -> {
            RoleRequestResponse requestResponse = new RoleRequestResponse(response, response.getRole().getRoleName());
            requestResponses.add(requestResponse);
        });
        return requestResponses;
    }

    @Override
    public RoleRequestResponse reviewRequest(Long id, ReviewStateDTO reviewState) {
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role request", "id", id)
        );
        AppUserEntity appUserEntity = getUserByRequest(userRepository);
        Set<Permissions> permissionsSet = getUserPermissions(appUserEntity);

        if (!permissionsSet.contains(MONITOR_ROLE_REQUEST)) {
            throw new LimitedPermissionException();
        } else if (!reviewState.getState().equals(ACCEPTED) && !reviewState.getState().equals(DECLINED)) {
            throw new IncorrectStateValueException("State can be either ACCEPTED or DECLINED");
        }

        AppUserEntity user = requestEntity.getUser();
        if (reviewState.getState().equals(ACCEPTED)) {
            user.setRole(requestEntity.getRole());
            userRepository.save(user);
        }

        if (reviewState.getDescription() != null) {
            requestEntity.setDescription(reviewState.getDescription());
        }

        requestEntity.setState(reviewState.getState());
        requestRepository.save(requestEntity);

        return new RoleRequestResponse(requestEntity, requestEntity.getRole().getRoleName());
    }

    //
    @Override
    public void deleteRequest(Long id) {
        // TODO: 9/2/22 there is enough to call existsById()
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role request", "id", id)
        );

        AppUserEntity currentUser = getUserByRequest(userRepository);
        Set<Permissions> permissionsSet = getUserPermissions(currentUser);
        if (permissionsSet.contains(MONITOR_ROLE_REQUEST)) {
            requestRepository.deleteById(id);
        } else {
            if (roleRequestBelongsUser(currentUser, requestEntity)) {
                requestRepository.deleteById(id);
            } else {
                throw new LimitedPermissionException();
            }
        }

        requestRepository.save(requestEntity);
    }

    boolean roleRequestBelongsUser(AppUserEntity user, RequestEntity requestEntity) {
        return Objects.equals(requestEntity.getUser().getId(), user.getId());
    }

    public Set<Permissions> getUserPermissions(AppUserEntity user) {
        return user.getRole().getPermissions();
    }
}
