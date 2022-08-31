package hamdam.bookee.APIs.roleRequest;

import com.auth0.jwt.interfaces.DecodedJWT;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUser;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import hamdam.bookee.tools.exeptions.InvalidRequestedRoleException;
import hamdam.bookee.tools.exeptions.InvalidRoleRequestException;
import hamdam.bookee.tools.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final AppRoleRepository roleRepository;
    private final AppUserRepository userRepository;

    @Override
    public RoleRequestResponse postRoleRequest(RequestRole requestRole, HttpServletRequest request) {
        RequestEntity requestEntity = new RequestEntity();

        AppUser user = getUser(request);
        validateUsersRole(user);

        AppRole role = roleRepository.findByRoleName(requestRole.getRoleName()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "role-name", requestRole.getRoleName()));
        validateRequestedRole(requestRole.getRoleName());

        requestEntity.setRole(role);
        requestEntity.setUser(user);
        requestEntity.setState(State.IN_PROGRESS);
        requestRepository.save(requestEntity);

        return new RoleRequestResponse(requestEntity, requestRole.getRoleName());
    }

    @Override
    public List<RoleRequestResponse> getAllRoleRequests(ReviewState reviewState, Long userId, HttpServletRequest request) {
        List<RequestEntity> responseList;

        AppUser appUser = userRepository.findAppUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", userId));

        AppUser requestedUser = getUser(request);
        if (requestedUser.getRole().getRoleName().equals("admin")) {
            responseList = checkReviewState(appUser, reviewState);
        } else if (requestedUser.getRole().getRoleName().equals("user")) {
            List<RequestEntity> requestEntities;
            if (reviewState.getState() == null) {
                requestEntities = requestRepository.findAll();
            } else {
                requestEntities  = requestRepository.findAllByState(reviewState.getState());
            }
            List<RequestEntity> usersRequests = new ArrayList<>();
            requestEntities.forEach(entity -> {
                if (entity.getUser().getUserName().equals(requestedUser.getUserName())) {
                    usersRequests.add(entity);
                }
            });
            responseList = usersRequests;
        } else {
            throw new RuntimeException("Only users with role [admin/user] have access for getting requests nigga.");
        }

        List<RoleRequestResponse> requestResponses = new ArrayList<>();
        responseList.forEach(entity -> {
            RoleRequestResponse response = new RoleRequestResponse(entity, "author");
            requestResponses.add(response);
        });
        return requestResponses;
    }

    @Override
    public RoleRequestResponse reviewRequest(Long id, ReviewState reviewState, HttpServletRequest request) {
        if (reviewState.getState() == null){
            throw new RuntimeException("State cannot be null");
        }
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role Request", "id", id));

        AppUser user = getUser(request);
        if (!user.getRole().getRoleName().equals("admin")){
            throw new RuntimeException("sho'rda bir exception yazibarish garak, 'admin bo'lmasang poydasi yo'q' dayan!");
        }
        Optional<AppRole> role = roleRepository.findByRoleName("author");
        switch (reviewState.getState()){
            case ACCEPTED:
                requestEntity.setState(State.ACCEPTED);
                requestEntity.getUser().setRole(role.get());
                break;
            case DECLINED:
                requestEntity.setState(State.DECLINED);
                break;
        }
        requestRepository.save(requestEntity);
        userRepository.save(user);
        return new RoleRequestResponse(requestEntity, "author");
    }

    private List<RequestEntity> checkReviewState(AppUser appUser, ReviewState reviewState){
        List<RequestEntity> responseList = new ArrayList<>();

        if (reviewState.getState() == null) {
            responseList = requestRepository.findAll();
        } else if (reviewState.getState().equals(State.IN_PROGRESS)) {
            responseList = requestRepository.findAllByState(State.IN_PROGRESS);
        } else if (reviewState.getState().equals(State.ACCEPTED)) {
            responseList = requestRepository.findAllByState(State.ACCEPTED);
        } else if (reviewState.getState().equals(State.DECLINED)) {
            responseList = requestRepository.findAllByState(State.DECLINED);
        }
        return responseList;
    }

    private AppUser getUser(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = TokenProvider.verifyToken(token, true);
        String username = decodedJWT.getSubject();

        AppUser user = userRepository.findAppUserByUserName(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
        return user;
    }

    private void validateUsersRole(AppUser user) {
        if (!user.getRole().getRoleName().equals("user")) {
            throw new InvalidRoleRequestException();
        }
    }

    private void validateRequestedRole(String role) {
        if (!role.equals("author")) {
            throw new InvalidRequestedRoleException();
        }
    }
}
