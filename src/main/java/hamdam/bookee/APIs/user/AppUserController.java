package hamdam.bookee.APIs.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.image.UserImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hamdam.bookee.tools.constants.Endpoints.API_USER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
//@RequestMapping(API_USER)
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService userService;

    @PostMapping("/api/v1/users")
    public AppUser addUser(@RequestBody AppUserDTO user){
        return userService.addUser(user);
    }

    @PatchMapping("/api/v1/users/set-role-to-user/{userId}")
    public AppUser addRoleToUser(@PathVariable long userId, @RequestBody AppUserRoleDTO appUserRoleDTO){
        return userService.setRoleToUser(userId, appUserRoleDTO);
    }

    @PatchMapping("/api/v1/users/set-image-to-user/{id}")
    public void setImageToUser(@PathVariable long id, @RequestBody UserImageDTO dto){
        userService.setImageToUser(id, dto);
    }

    @GetMapping("/api/v1/users/{name}")
    public AppUser getUserByName(@PathVariable String name){
        return userService.getUserByUsername(name);
    }

    @GetMapping("/api/v1/users")
    public List<AppUser> getAllUsers(){
        return userService.getAllUsers();
    }

    @PatchMapping("/api/v1/users/update/{id}")
    public AppUser updateUser(@RequestBody AppUser user, @PathVariable long id){
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/api/v1/users/{id}")
    public void deleteUser(@PathVariable long id){
        userService.deleteUser(id);
    }

    @GetMapping("/api/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getUserByUsername(username);
                String access_token = JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("role", user.getRoles().getRoleName())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

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
