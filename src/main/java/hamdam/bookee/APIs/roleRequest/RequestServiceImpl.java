package hamdam.bookee.APIs.roleRequest;

import com.auth0.jwt.interfaces.DecodedJWT;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUser;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
        Optional<AppRole> role = roleRepository.findByRoleName(requestRole.getRoleName());
        requestEntity.setRole(role.get());

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = TokenProvider.verifyToken(token, true);
        String username = decodedJWT.getSubject();
        Optional<AppUser> user = userRepository.findAppUserByUserName(username);
        requestEntity.setUser(user.get());
        requestEntity.setState(State.IN_PROGRESS);
        requestRepository.save(requestEntity);

        return new RoleRequestResponse(requestEntity, requestRole.getRoleName());
    }
}
