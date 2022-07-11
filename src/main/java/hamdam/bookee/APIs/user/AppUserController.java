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
    private final AppUserService userService;

    @PostMapping
    public void addUser(@RequestBody AppUserDTO user){
        userService.addUser(user);
    }

    @GetMapping("/{id}")
    public AppUser getUserByID(@PathVariable long id){
        return userService.getUserById(id);
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

    @PatchMapping("/set-image-to-user/{id}")
    public void setImageToUser(@PathVariable long id, @RequestBody UserImageDTO dto){
        userService.setImageToUser(id, dto);
    }
}
