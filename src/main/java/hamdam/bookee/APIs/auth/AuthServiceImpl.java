package hamdam.bookee.APIs.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUser;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
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

    public AppUser getUserByUsername(String userName) {
        return userRepository.findAppUserByUserName(userName).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
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
                // TODO 15.08.2022 here it throes exception, because username is null
                UserDetails user = (UserDetails)getUserByUsername(username);

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
            throw new RuntimeException("Refresh token is missing!");
        }
    }
}
