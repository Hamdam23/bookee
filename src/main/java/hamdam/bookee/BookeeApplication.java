package hamdam.bookee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
@RequiredArgsConstructor
public class BookeeApplication {

//    implements CommandLineRunner {
//    private final AppRoleService roleService;
//    private final AuthService authService;
//    private final AppUserService userService;

    @Bean
    @Primary
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookeeApplication.class, args);
    }

    // TODO: 9/2/22 this data initialization may be replaced by a migration script
//    @Override
//    public void run(String... args) {
//        try {
//            AppRoleResponseDTO admin = roleService.addRole(new AppRoleRequestDTO("admin", false, Set.of(
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
//            AppRoleResponseDTO author = roleService.addRole(new AppRoleRequestDTO("author", false, Set.of(
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
//            AppRoleResponseDTO user = roleService.addRole(new AppRoleRequestDTO("user", true, Set.of(
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
//            authService.registerUser(new RegistrationRequest("Farrukh", "farrukh_kh", "123"));
//            authService.registerUser(new RegistrationRequest("Hamdam", "hamdam_x", "123"));
//            authService.registerUser(new RegistrationRequest("Userx", "user_x", "123"));
//
//            userService.setRoleToUser(userService.getUserByUsername("farrukh_kh").getId(), new AppUserRoleIdDTO(admin.getId()));
//            userService.setRoleToUser(userService.getUserByUsername("hamdam_x").getId(), new AppUserRoleIdDTO(author.getId()));
//            userService.setRoleToUser(userService.getUserByUsername("user_x").getId(), new AppUserRoleIdDTO(user.getId()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
