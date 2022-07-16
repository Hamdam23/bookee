package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService userService;

    @PostMapping("/v1/users/post")
    public AppUser addUser(@RequestBody AppUserDTO user){
        return userService.addUser(user);
    }

    @PatchMapping("/v1/users/set-role-to-user/{userId}")
    public AppUser addRoleToUser(@PathVariable long userId, @RequestBody AppUserRoleDTO appUserRoleDTO){
        return userService.setRoleToUser(userId, appUserRoleDTO);
    }

    @PatchMapping("/v1/users/set-image-to-user/{id}")
    public void setImageToUser(@PathVariable long id, @RequestBody UserImageDTO dto){
        userService.setImageToUser(id, dto);
    }

    @GetMapping("/v1/users/{name}")
    public AppUser getUserByName(@PathVariable String name){
        return userService.getUserByUsername(name);
    }

    @GetMapping("/v1/users")
    public List<AppUser> getAllUsers(){
        return userService.getAllUsers();
    }

    @PatchMapping("/v1/users/update/{id}")
    public AppUser updateUser(@RequestBody AppUser user, @PathVariable long id){
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/v1/users/{id}")
    public void deleteUser(@PathVariable long id){
        userService.deleteUser(id);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.generateRefreshToken(request, response);
    }
}
