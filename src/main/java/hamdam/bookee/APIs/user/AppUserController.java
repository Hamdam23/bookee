package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_USER;

@RestController
@RequestMapping(API_USER)
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserServiceImpl userService;

    @PostMapping
    public void addUser(@RequestBody AppUserDTO user){
        userService.addUser(user);
    }

    @PatchMapping("/set-role-to-user/{userId}")
    public AppUser addRoleToUser(@PathVariable long userId, @RequestBody AppUserRoleDTO appUserRoleDTO){
        return userService.setRoleToUser(userId, appUserRoleDTO);
    }

    @PatchMapping("/set-image-to-user/{id}")
    public void setImageToUser(@PathVariable long id, @RequestBody UserImageDTO dto){
        userService.setImageToUser(id, dto);
    }

    @GetMapping("/{name}")
    public AppUser getUserByName(@PathVariable String name){
        return userService.getUserByName(name);
    }

    @GetMapping
    public List<AppUser> getAllUsers(){
        return userService.getAllUsers();
    }

    @PatchMapping("/update/{id}")
    public AppUser updateUser(@RequestBody AppUser user, @PathVariable long id){
        return userService.updateUser(user, id);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable long id){
        userService.deleteUser(id);
    }
}
