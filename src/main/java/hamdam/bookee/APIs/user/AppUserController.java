package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.*;

// TODO: 9/2/22 why API_ constant not used
@RestController
@RequestMapping(API_USER)
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService userService;

    // TODO: 9/2/22 order of methods

    @GetMapping
    public List<AppUserEntity> getAllUsers(){
        return userService.getAllUsers();
    }

    // TODO {name} should be replaced with {id}
    @GetMapping("/{name}")
    public AppUserEntity getUserByName(@PathVariable String name){
        return userService.getUserByUsername(name);
    }

    // TODO: 9/2/22 use DTO for request body
    @PatchMapping("/{id}")
    public AppUserEntity updateUser(@RequestBody AppUserDTO user, @PathVariable long id){
        return userService.updateUser(user, id);
    }

    // TODO {userId} should be replaced with {id}
    @PatchMapping(API_SET_ROLE_USER + "/{userId}")
    public AppUserEntity addRoleToUser(@PathVariable long userId, @RequestBody AppUserRoleDTO appUserRoleDTO){
        return userService.setRoleToUser(userId, appUserRoleDTO);
    }

    @PatchMapping(SET_IMAGE_TO_USER + "/{id}")
    public ResponseEntity<String> setImageToUser(@PathVariable long id, @RequestBody UserImageDTO dto){
        userService.setImageToUser(id, dto);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Image with id: " + dto.getImageId() +
                " is successfully set to User with id: " + id + ".");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        // TODO: 9/2/22 return full json response
        return ResponseEntity.ok().body("Successfully deleted the User with id: " + id + ".");
    }
}
