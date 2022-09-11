package hamdam.bookee.APIs.roleRequest;

import com.auth0.jwt.interfaces.DecodedJWT;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exeptions.pemission.NoCorrespondingPermissionException;
import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import hamdam.bookee.tools.exeptions.roleRequest.UnsupportedStateValueException;
import hamdam.bookee.tools.exeptions.roleRequest.UnsupportedRequestedRoleName;
import hamdam.bookee.tools.exeptions.roleRequest.UnsupportedUserOnRoleRequest;
import hamdam.bookee.tools.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.*;
import static hamdam.bookee.APIs.roleRequest.State.ACCEPTED;
import static hamdam.bookee.APIs.roleRequest.State.DECLINED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final AppRoleRepository roleRepository;
    private final AppUserRepository userRepository;

    @Override
    public RoleRequestResponse postRoleRequest(RequestRole requestRole, HttpServletRequest request) {
        // TODO: 9/2/22 why are you creating RequestEntity here? Create it where it needed with constructor arguments
        //  Done

        // TODO: 9/2/22 why using setters? use them as constructor arguments
        //  Done

        AppUserEntity appUserEntity = getUserByRequest(request);
        AppRoleEntity role = roleRepository.findByRoleName(requestRole.getRoleName()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "role-name", requestRole.getRoleName()));
        Set<Permissions> permissionsSet = getUserPermissions(appUserEntity);

        if (!permissionsSet.contains(USER)) {
            throw new UnsupportedUserOnRoleRequest();
        } else if (role.getPermissions().contains(ADMIN)) {
            throw new UnsupportedRequestedRoleName();
        }
        RequestEntity requestEntity = new RequestEntity(appUserEntity, role, State.IN_PROGRESS);
        requestRepository.save(requestEntity);

        return new RoleRequestResponse(requestEntity, requestRole.getRoleName());
    }

    // TODO: 9/2/22 rename (gets not all role requests in some situations)
    // TODO: 9/2/22 method needs big code refactor
    // TODO: 9/2/22 add feature: admin (not user with roleName="admin", but user with appropriate permission) can see role requests of specific user or all users
    @Override
    public List<RoleRequestResponse> getAllRoleRequests(State reviewState, HttpServletRequest request) {
        List<RequestEntity> responseList;
        if (reviewState == null) {
            responseList = requestRepository.findAll();
        } else {
            responseList = requestRepository.findAllByState(reviewState);
        }

        AppUserEntity appUserEntity = getUserByRequest(request);
        Set<Permissions> permissionsSet = getUserPermissions(appUserEntity);
        // change ADMIN permission -> MONITOR_ROLE_REQUEST
        if (!permissionsSet.contains(ADMIN) && !responseList.isEmpty()) {
            responseList = requestRepository.findAllByUser(appUserEntity);
        }
        List<RoleRequestResponse> requestResponses = new ArrayList<>();
        responseList.forEach(response -> {
            RoleRequestResponse requestResponse = new RoleRequestResponse(response, response.getRole().getRoleName());
            requestResponses.add(requestResponse);
        });
        return requestResponses;
    }

    // TODO: 9/2/22 static use of AppRole!
    // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
    // TODO: 9/2/22 there must be other case(s) in switch
    // TODO: 9/2/22 why .setState() call inside case?
    // TODO: 9/2/22 handle get() call
    @Override
    public RoleRequestResponse reviewRequest(Long id, State reviewState, HttpServletRequest request) {
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role request", "id", id)
        );
        AppUserEntity appUserEntity = getUserByRequest(request);
        Set<Permissions> permissionsSet = getUserPermissions(appUserEntity);

        if (!permissionsSet.contains(ADMIN)) {
            throw new NoCorrespondingPermissionException("You have to get corresponding permission to access the method!");
        } else if (reviewState == null ||
                (!reviewState.equals(ACCEPTED) &&
                        !reviewState.equals(DECLINED))
        ) {
            throw new UnsupportedStateValueException("state can be either ACCEPTED or DECLINED");
        }

        AppUserEntity user = requestEntity.getUser();

        requestEntity.setState(reviewState);

        if (reviewState.equals(ACCEPTED)) {
            user.setRole(requestEntity.getRole());
        }

        requestRepository.save(requestEntity);
        userRepository.save(user);
        return new RoleRequestResponse(requestEntity, requestEntity.getRole().getRoleName());
    }

    //
    @Override
    public void deleteRequest(Long id, HttpServletRequest request) {
        // TODO: 9/2/22 there is enough to call existsById()
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role request", "id", id)
        );

        AppUserEntity appUserEntity = getUserByRequest(request);
        Set<Permissions> permissionsSet = getUserPermissions(appUserEntity);
        if (permissionsSet.contains(ADMIN)) {
            requestRepository.deleteById(id);
        } else {
            if (roleRequestBelongUser(appUserEntity, requestEntity)){
                requestRepository.deleteById(id);
            } else {
                throw new NoCorrespondingPermissionException("You do not have permission to delete role request does not belong to you!");
            }
        }

        requestRepository.save(requestEntity);
    }

    private boolean roleRequestBelongUser(AppUserEntity user, RequestEntity requestEntity){
        return Objects.equals(requestEntity.getUser().getId(), user.getId());
    }

    public AppUserEntity getUserByRequest(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = TokenProvider.decodeToken(token, true);
        String username = decodedJWT.getSubject();

        return userRepository.findAppUserByUserName(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
    }

    public Set<Permissions> getUserPermissions(AppUserEntity user) {
        return user.getRole().getPermissions();
    }
}
