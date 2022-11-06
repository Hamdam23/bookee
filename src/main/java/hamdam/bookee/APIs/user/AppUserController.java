package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.SetUserPasswordDTO;
import hamdam.bookee.APIs.user.helpers.SetUserRoleDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.paging.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hamdam.bookee.tools.constants.Endpoints.*;

@RestController
@RequestMapping(API_USER)
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;

    @GetMapping
    public PagedResponse<AppUserResponseDTO> getAllUsers(@PageableDefault Pageable pageable) {
        return new PagedResponse<>(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public AppUserResponseDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    public AppUserResponseDTO updateUser(@RequestBody AppUserRequestDTO user, @PathVariable Long id) {
        return userService.updateUser(user, id);
    }

    @PatchMapping("/change-password/{id}")
    public AppUserResponseDTO updatePassword(@RequestBody SetUserPasswordDTO passwordDTO, @PathVariable Long id) {
        return userService.updatePassword(passwordDTO, id);
    }

    @PatchMapping(API_SET_ROLE_USER + "/{id}")
    public AppUserResponseDTO addRoleToUser(@PathVariable Long id, @RequestBody SetUserRoleDTO setUserRoleDTO) {
        return userService.setRoleToUser(id, setUserRoleDTO);
    }

    @PatchMapping(SET_IMAGE_TO_USER + "/{id}")
    public AppUserResponseDTO setImageToUser(@PathVariable Long id, @RequestBody UserImageDTO dto) {
        userService.setImageToUser(id, dto);
        return userService.setImageToUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.NO_CONTENT);
    }
}
