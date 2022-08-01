package hamdam.bookee;

import hamdam.bookee.APIs.auth.AuthService;
import hamdam.bookee.APIs.auth.AuthUserDTO;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleDTO;
import hamdam.bookee.APIs.role.AppRoleService;
import hamdam.bookee.APIs.role.Permission;
import hamdam.bookee.APIs.user.AppUser;
import hamdam.bookee.APIs.user.AppUserRoleDTO;
import hamdam.bookee.APIs.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

import static hamdam.bookee.APIs.role.Permission.*;

@SpringBootApplication
@RequiredArgsConstructor
public class BookeeApplication implements CommandLineRunner {

    private final AppRoleService roleService;
    private final AuthService authService;
    private final AppUserService userService;

    public static void main(String[] args) {
        SpringApplication.run(BookeeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            AppRole author = roleService.addRole(new AppRoleDTO("author", false, Set.of(Permission.values())));
            AppRole admin = roleService.addRole(new AppRoleDTO("admin", false, Set.of(
                    GET_BOOK,
                    CREATE_BOOK,
                    UPDATE_BOOK,
                    DELETE_BOOK,
                    GET_GENRE,
                    CREATE_GENRE,
                    UPDATE_GENRE,
                    DELETE_GENRE,
                    GET_ROLE,
                    GET_USER,
                    CREATE_USER,
                    UPDATE_USER
            )));
            AppRole user = roleService.addRole(new AppRoleDTO("user", true, Set.of(
                    GET_BOOK,
                    CREATE_BOOK,
                    UPDATE_BOOK,
                    DELETE_BOOK,
                    GET_GENRE,
                    GET_ROLE,
                    GET_USER,
                    CREATE_USER,
                    UPDATE_USER
            )));

            AppUser hamdam = authService.addUser(new AuthUserDTO("Hamdam", "hamdam_x", "123"));
            AppUser farrukh = authService.addUser(new AuthUserDTO("Farrukh", "farrukh_kh", "123"));
            AppUser userov = authService.addUser(new AuthUserDTO("Userov", "user_ov", "123"));

            userService.setRoleToUser(hamdam.getId(), new AppUserRoleDTO(author.getId()));
            userService.setRoleToUser(farrukh.getId(), new AppUserRoleDTO(admin.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
