package hamdam.bookee;

import hamdam.bookee.APIs.auth.AuthService;
import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleDTO;
import hamdam.bookee.APIs.role.AppRoleService;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.user.AppUser;
import hamdam.bookee.APIs.user.AppUserRoleDTO;
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
//            AppRole author = roleService.addRole(new AppRoleDTO("author", false, Set.of(AUTHOR)));
//            AppRole admin = roleService.addRole(new AppRoleDTO("admin", false, Set.of(
//                    ADMIN
//            )));
//            AppRole user = roleService.addRole(new AppRoleDTO("user", true, Set.of(
//                    USER
//            )));
//
//            AppUser hamdam = authService.registerUser(new RegistrationRequest("Hamdam", "hamdam_x", "123"));
//            AppUser farrukh = authService.registerUser(new RegistrationRequest("Farrukh", "farrukh_kh", "123"));
//            AppUser userx = authService.registerUser(new RegistrationRequest("Userx", "user_x", "123"));
//
//            userService.setRoleToUser(hamdam.getId(), new AppUserRoleDTO(author.getId()));
//            userService.setRoleToUser(farrukh.getId(), new AppUserRoleDTO(admin.getId()));
//            userService.setRoleToUser(userx.getId(), new AppUserRoleDTO(user.getId()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
