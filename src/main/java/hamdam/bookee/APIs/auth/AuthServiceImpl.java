package hamdam.bookee.APIs.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUser;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.AppUserServiceImpl;
import hamdam.bookee.tools.exeptions.RefreshTokenMissingException;
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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final AppUserServiceImpl appUserServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser registerUser(RegistrationRequest userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        AppUser appUser = new AppUser(userDTO);
        AppRole role = roleRepository.findFirstByIsDefault(true).orElseThrow(
                () -> new RuntimeException("There is no default role for users")
        );
        appUser.setRole(role);
        userRepository.save(appUser);
        return appUser;
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
//                Algorithm checkerAlgorithm = Algorithm.HMAC384("secret".getBytes());
//                JWTVerifier verifier = JWT.require(checkerAlgorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                DecodedJWT decodedJWT = TokenProvider.verifyToken(refresh_token, false);
                String username = decodedJWT.getSubject();
                UserDetails user = appUserServiceImpl.loadUserByUsername(username);

                AccessTResponse accessTResponse = TokenProvider.generateAToken(user, userRepository);
                TokenProvider.sendAToken(accessTResponse, response);

//                Algorithm senderAlgorithm = Algorithm.HMAC256("secret".getBytes());
//                String access_token = JWT.create()
//                        .withSubject(user.getUserName())
//                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
//                        .withClaim("permissions", user.getRole().getPermissions().stream().map(Enum::name).collect(Collectors.toList()))
//                        .sign(senderAlgorithm);
//
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RefreshTokenMissingException("Refresh token is missing!");
        }
    }
}
