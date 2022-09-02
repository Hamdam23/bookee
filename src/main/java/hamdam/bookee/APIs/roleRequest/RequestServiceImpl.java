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
        // TODO: 9/2/22 why are you creating RequestEntity here? Create it where it needed with constructor arguments
        RequestEntity requestEntity = new RequestEntity();

        AppUser user = getUser(request);
        validateUsersRole(user);

        AppRole role = roleRepository.findByRoleName(requestRole.getRoleName()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "role-name", requestRole.getRoleName()));
        validateRequestedRole(requestRole.getRoleName());

        // TODO: 9/2/22 why using setters? use them as constructor arguments
        requestEntity.setRole(role);
        requestEntity.setUser(user);
        requestEntity.setState(State.IN_PROGRESS);
        requestRepository.save(requestEntity);

        return new RoleRequestResponse(requestEntity, requestRole.getRoleName());
    }

    // TODO: 9/2/22 rename (gets not all role requests in some situations)
    // TODO: 9/2/22 method needs big code refactor
    // TODO: 9/2/22 add feature: admin (not user with roleName="admin", but user with appropriate permission) can see role requests of specific user or all users
    @Override
    public List<RoleRequestResponse> getAllRoleRequests(ReviewState reviewState, HttpServletRequest request) {
        List<RequestEntity> responseList;

        AppUser requestedUser = getUser(request);
        // TODO: 9/2/22 static use of AppRole!
        // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
        if (requestedUser.getRole().getRoleName().equals("admin")) {
            // TODO: 9/2/22 do you really need if/else for calling checkReviewState
            responseList = checkReviewState(reviewState);
            // TODO: 9/2/22 static use of AppRole!
            // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
        } else if (requestedUser.getRole().getRoleName().equals("user")) {
            List<RequestEntity> requestEntities;
            if (reviewState.getState() == null) {
                requestEntities = requestRepository.findAll();
            } else {
                requestEntities  = requestRepository.findAllByState(reviewState.getState());
            }
            // TODO: 9/2/22 why so many boilerplate code!
            // TODO: 9/2/22 add and use repository custom methods!
            List<RequestEntity> usersRequests = new ArrayList<>();
            requestEntities.forEach(entity -> {
                if (entity.getUser().getUserName().equals(requestedUser.getUserName())) {
                    usersRequests.add(entity);
                }
            });
            responseList = usersRequests;
        } else {
            // TODO: 9/2/22 custom exception (with dynamic message, because AppRole is dynamic)
            throw new RuntimeException("Only users with role [admin/user] have access for getting requests nigga.");
        }

        List<RoleRequestResponse> requestResponses = new ArrayList<>();
        responseList.forEach(entity -> {
            // TODO: 9/2/22 static use of AppRole!
            // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
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
        // TODO: 9/2/22 static use of AppRole!
        // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
        if (!user.getRole().getRoleName().equals("admin")){
            // TODO: 9/2/22 custom exception
            throw new RuntimeException("sho'rda bir exception yazibarish garak, 'admin bo'lmasang poydasi yo'q' dayan!");
        }
        // TODO: 9/2/22 static use of AppRole!
        // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
        Optional<AppRole> role = roleRepository.findByRoleName("author");
        // TODO: 9/2/22 there must be other case(s) in switch
        switch (reviewState.getState()){
            case ACCEPTED:
                // TODO: 9/2/22 why .setState() call inside case?
                requestEntity.setState(State.ACCEPTED);
                // TODO: 9/2/22 handle get() call
                requestEntity.getUser().setRole(role.get());
                break;
            case DECLINED:
                // TODO: 9/2/22 why .setState() call inside case?
                requestEntity.setState(State.DECLINED);
                break;
        }
        requestRepository.save(requestEntity);
        userRepository.save(user);
        // TODO: 9/2/22 static use of AppRole!
        // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
        return new RoleRequestResponse(requestEntity, "author");
    }

    @Override
    public void deleteRequest(Long id, HttpServletRequest request) {
        // TODO: 9/2/22 there is enough to call existsById()
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role Request", "id", id));

        AppUser user = getUser(request);
        // TODO: 9/2/22 static use of AppRole!
        // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
        if (user.getRole().getRoleName().equals("admin")){
            requestRepository.deleteById(id);
        } else {
            // TODO: 9/2/22 custom exception
            throw new RuntimeException("sho'rda bir exception yazibarish garak, 'admin bo'lmasang poydasi yo'q' dayan!");
        }
    }

    // TODO: 9/2/22 needs rename (!)
    private List<RequestEntity> checkReviewState(ReviewState reviewState){
        List<RequestEntity> responseList = new ArrayList<>();

        // TODO: why checking state value in else if? isn't it enough to pass state value to repository!
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

    // TODO: 9/2/22 rename
    private AppUser getUser(HttpServletRequest request){
        // TODO: 9/2/22 code duplication
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = TokenProvider.verifyToken(token, true);
        String username = decodedJWT.getSubject();

        return userRepository.findAppUserByUserName(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
    }

    // TODO: 9/2/22 only users with "user" role can change their role? why?
    // TODO: 9/2/22 what if, in future you will add to project new role: like expert reviewer of the books.
    // TODO: 9/2/22 can expert reviewer become author? why?
    private void validateUsersRole(AppUser user) {
        // TODO: 9/2/22 static use of AppRole!
        // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
        if (!user.getRole().getRoleName().equals("user")) {
            throw new InvalidRoleRequestException();
        }
    }

    private void validateRequestedRole(String role) {
        // TODO: 9/2/22 static use of AppRole!
        // TODO: 9/2/22 AppRole is dynamic, use it dynamically!
        if (!role.equals("author")) {
            throw new InvalidRequestedRoleException();
        }
    }
}
