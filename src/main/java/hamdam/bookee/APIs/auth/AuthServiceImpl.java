package hamdam.bookee.APIs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.AppUserService;
import hamdam.bookee.tools.exceptions.DuplicateResourceException;
import hamdam.bookee.tools.exceptions.role.NoDefaultRoleException;
import hamdam.bookee.tools.token.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static hamdam.bookee.tools.token.TokenChecker.checkHeader;
import static hamdam.bookee.tools.token.TokenUtils.getTokenResponse;
import static hamdam.bookee.tools.token.TokenUtils.getUsernameFromToken;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokensResponse registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByUserName(registrationRequest.getUsername())) {
            throw new DuplicateResourceException("username");
        }
        AppUserEntity appUserEntity = new AppUserEntity(registrationRequest);
        appUserEntity.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        AppRoleEntity role = roleRepository.findFirstByIsDefaultIsTrue()
                .orElseThrow(() -> new NoDefaultRoleException("There is no default role for users"));
        appUserEntity.setRole(role);

        userRepository.save(appUserEntity);
        return getTokenResponse(registrationRequest.getUsername(), userRepository);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String header = request.getHeader(AUTHORIZATION);
        checkHeader(header, false);

        try {
            UserDetails user = appUserService.loadUserByUsername(getUsernameFromToken(header));
            TokensResponse accessTokenResponse = TokenUtils.getAccessTokenResponse(user.getUsername(), userRepository);
            TokenUtils.presentToken(accessTokenResponse, response);
        } catch (Exception exception) {
            response.setHeader("error", exception.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_error_message", exception.getMessage());
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}
