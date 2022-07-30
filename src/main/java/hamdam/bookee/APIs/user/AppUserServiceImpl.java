package hamdam.bookee.APIs.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.image.Image;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO here, when User is not found Exception does not work anyway,because Security EntryPoint Ex. will execute.
        //AppUser user = userRepository.findAppUserByUserName(username).orElseThrow(()
        //-> new RuntimeException("User not found!")
        //);
        Optional<AppUser> user = userRepository.findAppUserByUserName(username);
        Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
        //TODO Here is where it catches the exception.
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.get().getRole().getRoleName());
        authority.add(simpleGrantedAuthority);
        return new User(user.get().getUserName(), user.get().getPassword(), authority);
    }

    @Override
    public AppUser addUser(AppUserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        AppUser appUser = new AppUser(userDTO);

        // todo security authorization (not authentication - login) not working because of this line
        //         there default role is set ro ROLE_USER, but in securing endpoints in SecurityConfiguration
        //          the required role is APP_USER. You can simply fix this by using only one of them

        // TODO IMHO(Farrukh): this inconsistency with role names is appearing because AppRole in your project is neither fully dynamic,
        // nor fully static. You are using entity (and db table) for saving roles, but giving role names to security statically
        // Possible solution: use fully static roles as in Progee-API or use fully dynamic roles as in edVantage
        AppRole role = roleRepository.findAppRoleByRoleName("ROLE_USER");
        appUser.setRole(role);
        appUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(appUser);
        return appUser;
    }

    @Override
    public AppUser getUserByUsername(String userName) {
        return userRepository.findAppUserByUserName(userName).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public AppUser updateUser(AppUser newUser, long id) {
        AppUser user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
        user.setName(newUser.getName());
        user.setPassword(newUser.getPassword());
        user.setUpdateAt(LocalDateTime.now());
        return user;
    }

    @Override
    public void deleteUser(long id) {
        imageRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", id)
        );
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setImageToUser(long id, UserImageDTO imageDTO) {
        Image image = imageRepository.findById(imageDTO.getImageId()).orElseThrow(()
                -> new ResourceNotFoundException("Image", "id", id)
        );
        AppUser user = userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", imageDTO.getImageId())
        );
        user.setUserImage(image);
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public AppUser setRoleToUser(long id, AppUserRoleDTO appUserRoleDTO) {
        AppUser user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
        AppRole appRole = roleRepository.findAppRoleById(appUserRoleDTO.getRoleId());
        user.setRole(appRole);
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    @Override
    public void generateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = getUserByUsername(username);
                String access_token = JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", Collections.singletonList(user.getRole().getRoleName()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
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
