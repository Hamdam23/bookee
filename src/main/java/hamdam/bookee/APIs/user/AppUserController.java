package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: 9/2/22 why API_ constant not used
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService userService;

    // TODO: 9/2/22 order of methods

    @PatchMapping("/users/set-role-to-user/{userId}")
    public AppUser addRoleToUser(@PathVariable long userId, @RequestBody AppUserRoleDTO appUserRoleDTO){
        return userService.setRoleToUser(userId, appUserRoleDTO);
    }

    @PatchMapping("/users/set-image-to-user/{id}")
    public ResponseEntity<String> setImageToUser(@PathVariable long id, @RequestBody UserImageDTO dto){
        userService.setImageToUser(id, dto);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Image with id: " + dto.getImageId() +
                " is successfully set to User with id: " + id + ".");
    }

    // TODO: 9/2/22 use DTO for request body
    @PatchMapping("/users/update/{id}")
    public AppUser updateUser(@RequestBody AppUser user, @PathVariable long id){
        return userService.updateUser(user, id);
    }

    @GetMapping("/users/{name}")
    public AppUser getUserByName(@PathVariable String name){
        return userService.getUserByUsername(name);
    }

    @GetMapping("/users")
    public List<AppUser> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Successfully deleted the User with id: " + id + ".");
    }
}
