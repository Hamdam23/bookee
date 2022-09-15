package hamdam.bookee;

import hamdam.bookee.APIs.auth.AuthService;
import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleService;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.helpers.AppUserRoleIdDTO;
import hamdam.bookee.APIs.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.*;

@SpringBootApplication
@RequiredArgsConstructor
public class BookeeApplication {

//    implements CommandLineRunner
//    private final AppRoleService roleService;
//    private final AuthService authService;
//    private final AppUserService userService;

    public static void main(String[] args) {
        SpringApplication.run(BookeeApplication.class, args);
    }

    // TODO: 9/2/22 this data initialization may be replaced by a migration script
//    @Override
//    public void run(String... args) {
//        try {
//            AppRoleEntity admin = roleService.addRole(new AppRoleRequestDTO("admin", false, Set.of(
//                    MONITOR_USER,
//                    MONITOR_ROLE,
//                    MONITOR_ROLE_REQUEST,
//
//                    GET_BOOK,
//                    CREATE_BOOK,
//                    UPDATE_BOOK,
//                    DELETE_BOOK,
//
//                    GET_GENRE,
//                    CREATE_GENRE,
//                    UPDATE_GENRE,
//                    DELETE_GENRE
//            )));
//            AppRoleEntity author = roleService.addRole(new AppRoleRequestDTO("author", false, Set.of(
//                    GET_USER,
//                    UPDATE_USER,
//                    DELETE_USER,
//
//                    GET_BOOK,
//                    CREATE_BOOK,
//                    UPDATE_BOOK,
//                    DELETE_BOOK,
//
//                    GET_GENRE,
//                    CREATE_GENRE,
//                    UPDATE_GENRE,
//                    DELETE_GENRE
//            )));
//            AppRoleEntity user = roleService.addRole(new AppRoleRequestDTO("user", true, Set.of(
//                    GET_USER,
//                    UPDATE_USER,
//                    DELETE_USER,
//
//                    GET_ROLE_REQUEST,
//                    CREATE_ROLE_REQUEST,
//                    UPDATE_ROLE_REQUEST,
//                    DELETE_ROLE_REQUEST,
//
//                    GET_BOOK,
//                    GET_GENRE
//            )));
//
//            AppUserEntity farrukh = authService.registerUser(new RegistrationRequest("Farrukh", "farrukh_kh", "123"));
//            AppUserEntity hamdam = authService.registerUser(new RegistrationRequest("Hamdam", "hamdam_x", "123"));
//            AppUserEntity userx = authService.registerUser(new RegistrationRequest("Userx", "user_x", "123"));
//
//            userService.setRoleToUser(farrukh.getId(), new AppUserRoleIdDTO(admin.getId()));
//            userService.setRoleToUser(hamdam.getId(), new AppUserRoleIdDTO(author.getId()));
//            userService.setRoleToUser(userx.getId(), new AppUserRoleIdDTO(user.getId()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
