package hamdam.bookee.APIs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.AppUserServiceImpl;
import hamdam.bookee.tools.exeptions.role.NoDefaultRoleException;
import hamdam.bookee.tools.token.TokenProvider;
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
import static hamdam.bookee.tools.token.TokenProvider.getUsernameFromToken;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    // TODO: 9/2/22 don't use implementation of bean when injecting dependency
    //  because I need loadUserByUsername method, and AppUserService doesn't have such method
    private final AppUserServiceImpl appUserServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Override
    // TODO: 9/2/22 naming: userDTO
    public AppUserEntity registerUser(RegistrationRequest request) {
        // TODO: 9/2/22 set encoded password to AppUser, not to RegistrationRequest
        AppUserEntity appUserEntity = new AppUserEntity(request);
        appUserEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        AppRoleEntity role = roleRepository.findFirstByIsDefault(true).orElseThrow(
                // TODO: 9/2/22 use custom exception
                () -> new NoDefaultRoleException("There is no default role for users")
        );
        appUserEntity.setRole(role);
        // TODO: 9/2/22 you can return value which is being returned by repository method
        return userRepository.save(appUserEntity);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String header = request.getHeader(AUTHORIZATION);
        // TODO: 9/2/22 code duplication: CustomAuthorizationFilter:doFilterInternal
        checkHeader(header);

        try {
//            String refresh_token = header.substring("Bearer ".length());
//            // TODO catch error "The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA384"
//            //  code 124
//            DecodedJWT decodedJWT = TokenProvider.decodeToken(refresh_token, false);
//            String username = decodedJWT.getSubject();
            UserDetails user = appUserServiceImpl.loadUserByUsername(getUsernameFromToken(header));

            TokensResponse accessTokenResponse = TokenProvider.getAccessTokenResponse(user.getUsername(), userRepository);
            TokenProvider.displayToken(accessTokenResponse, response);
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
