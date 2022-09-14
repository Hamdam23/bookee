package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.user.helpers.AppUserRoleIdDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.paging.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static hamdam.bookee.tools.constants.Endpoints.*;

// TODO: 9/2/22 why API_ constant not used
@RestController
@RequestMapping(API_USER)
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;
    // TODO: 9/2/22 order of methods

    @GetMapping
    public PagedResponse<AppUserResponseDTO> getAllUsers(@PageableDefault Pageable pageable) {
        return new PagedResponse<>(userService.getAllUsers(pageable));
    }

    // TODO {name} should be replaced with {id}
    @GetMapping("/{id}")
    public AppUserResponseDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // TODO: 9/2/22 use DTO for request body
    @PatchMapping("/{id}")
    public AppUserResponseDTO updateUser(@RequestBody AppUserRequestDTO user, @PathVariable Long id, HttpServletRequest request) {
        return userService.updateUser(user, id, request);
    }

    // TODO {userId} should be replaced with {id}
    @PatchMapping(API_SET_ROLE_USER + "/{userId}")
    public AppUserResponseDTO addRoleToUser(@PathVariable Long userId, @RequestBody AppUserRoleIdDTO appUserRoleIdDTO) {
        return userService.setRoleToUser(userId, appUserRoleIdDTO);
    }

    @PatchMapping(SET_IMAGE_TO_USER + "/{id}")
    public AppUserResponseDTO setImageToUser(@PathVariable Long id, @RequestBody UserImageDTO dto) {
        userService.setImageToUser(id, dto);
        // TODO: 9/2/22 return full json response
        return userService.setImageToUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id, HttpServletRequest request) {

        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(userService.deleteUser(id, request), HttpStatus.NO_CONTENT);
    }
}
