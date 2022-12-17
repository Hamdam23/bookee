package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.user.helpers.SetImageUserRequest;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.PasswordUpdateRequest;
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

    /**
     * It returns a paged response of all users
     *
     * @param pageable This is a Spring Data interface that allows us to pass in pagination parameters.
     * @return A PagedResponse object containing a list of AppUserResponseDTO objects.
     */
    @GetMapping
    public PagedResponse<AppUserResponseDTO> getAllUsers(@PageableDefault Pageable pageable) {
        return new PagedResponse<>(userService.getAllUsers(pageable));
    }

    /**
     * It takes a Long id as a path variable, and returns an AppUserResponseDTO
     *
     * @param id The id of the user you want to get.
     * @return AppUserResponseDTO
     */
    @GetMapping("/{id}")
    public AppUserResponseDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * It updates the user with the given id.
     *
     * @param user The user object that will be updated.
     * @param id The id of the user to be updated.
     * @return AppUserResponseDTO
     */
    @PatchMapping("/{id}")
    public AppUserResponseDTO updateUser(@RequestBody AppUserRequestDTO user, @PathVariable Long id) {
        return userService.updateUser(user, id);
    }

    /**
     * It takes in a request body of type UpdatePasswordRequest and a path variable of type Long, and
     * returns an AppUserResponseDTO
     *
     * @param passwordDTO This is the object that contains the new password and the old password.
     * @param id The id of the user whose password is to be changed.
     * @return AppUserResponseDTO
     */
    @PatchMapping("/change-password/{id}")
    public AppUserResponseDTO updatePassword(@RequestBody PasswordUpdateRequest passwordDTO, @PathVariable Long id) {
        return userService.updatePassword(passwordDTO, id);
    }

    /**
     * It sets the role of a user.
     *
     * @param id The id of the user to be updated
     * @param setRoleUserRequest
     * @return AppUserResponseDTO
     */
    @PatchMapping(API_SET_ROLE_USER + "/{id}")
    public AppUserResponseDTO setRoleToUser(@PathVariable Long id, @RequestBody SetRoleUserRequest setRoleUserRequest) {
        return userService.setRoleToUser(id, setRoleUserRequest);
    }

    /**
     * It takes a user id and a UserImageDTO object, and returns an AppUserResponseDTO object
     *
     * @param id The id of the user to whom the image will be set.
     * @param dto the image that will be set to the user
     * @return AppUserResponseDTO
     */
    @PatchMapping(SET_IMAGE_TO_USER + "/{id}")
    public AppUserResponseDTO setImageToUser(@PathVariable Long id, @RequestBody SetImageUserRequest dto) {
        return userService.setImageToUser(id, dto);
    }

    /**
     * It deletes a user with the given id and returns a response entity with a status code of 200 and
     * a message saying that the user was deleted
     *
     * @param id The id of the user to be deleted.
     * @return A ResponseEntity object is being returned.
     */
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
