package hamdam.bookee;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

//    @Override
//    public void run(String... args) {
//        try {
//            AppRole author = roleService.addRole(new AppRoleDTO("author", false, Set.of(Permission.values())));
//            AppRole admin = roleService.addRole(new AppRoleDTO("admin", false, Set.of(
//                    GET_BOOK,
//                    CREATE_BOOK,
//                    UPDATE_BOOK,
//                    DELETE_BOOK,
//                    GET_GENRE,
//                    CREATE_GENRE,
//                    UPDATE_GENRE,
//                    DELETE_GENRE,
//                    GET_ROLE,
//                    GET_USER,
//                    CREATE_USER,
//                    UPDATE_USER
//            )));
//            AppRole user = roleService.addRole(new AppRoleDTO("user", true, Set.of(
//                    GET_BOOK,
//                    CREATE_BOOK,
//                    UPDATE_BOOK,
//                    DELETE_BOOK,
//                    GET_GENRE,
//                    GET_ROLE,
//                    GET_USER,
//                    CREATE_USER,
//                    UPDATE_USER
//            )));
//
//            AppUser hamdam = authService.addUser(new AuthUserDTO("Hamdam", "hamdam_x", "123"));
//            AppUser farrukh = authService.addUser(new AuthUserDTO("Farrukh", "farrukh_kh", "123"));
//            AppUser userx = authService.addUser(new AuthUserDTO("Userx", "user_x", "123"));
//
//            userService.setRoleToUser(hamdam.getId(), new AppUserRoleDTO(author.getId()));
//            userService.setRoleToUser(farrukh.getId(), new AppUserRoleDTO(admin.getId()));
//            userService.setRoleToUser(userx.getId(), new AppUserRoleDTO(user.getId()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
