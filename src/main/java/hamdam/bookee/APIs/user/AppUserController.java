package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.helpers.UserImageDTO;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.UpdatePasswordRequest;
import hamdam.bookee.APIs.user.helpers.SetRoleUserRequest;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.paging.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static hamdam.bookee.tools.constants.DeletionMessage.getDeletionMessage;
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
    public AppUserResponseDTO updatePassword(@RequestBody UpdatePasswordRequest passwordDTO, @PathVariable Long id) {
        return userService.updatePassword(passwordDTO, id);
    }

    @PatchMapping(API_SET_ROLE_USER + "/{id}")
    public AppUserResponseDTO setRoleToUser(@PathVariable Long id, @RequestBody SetRoleUserRequest setRoleUserRequest) {
        return userService.setRoleToUser(id, setRoleUserRequest);
    }

    @PatchMapping(SET_IMAGE_TO_USER + "/{id}")
    public AppUserResponseDTO setImageToUser(@PathVariable Long id, @RequestBody UserImageDTO dto) {
        return userService.setImageToUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK,
                        LocalDateTime.now(),
                        getDeletionMessage("User", id)
                ), HttpStatus.OK
        );
    }
}
