package hamdam.bookee.APIs.role;

import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
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
import static hamdam.bookee.tools.constants.Endpoints.API_ROLE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_ROLE)
public class AppRoleController {

    private final AppRoleService appRoleService;

    /**
     * The function takes in a request body of type AppRoleRequestDTO,
     * and returns a response body of type AppRoleResponseDTO
     *
     * @param appRole This is the request body that is sent to the server.
     * @return AppRoleResponseDTO
     */
    @PostMapping
    public AppRoleResponseDTO postRole(@RequestBody AppRoleRequestDTO appRole) {
        return appRoleService.addRole(appRole);
    }

    /**
     * It returns a list of all roles in the database.
     *
     * @param pageable This is a Spring Data interface that allows us to pass pagination
     * and sorting parameters to our query.
     * @return A PagedResponse object containing a list of AppRoleResponseDTO objects.
     */
    @GetMapping
    public PagedResponse<AppRoleResponseDTO> getAllRoles(@PageableDefault Pageable pageable) {
        return new PagedResponse<>(appRoleService.getAllRoles(pageable));
    }

    /**
     * It deletes a role by id.
     *
     * @param id The id of the role to be deleted
     * @return A ResponseEntity object is being returned.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteRoleById(@PathVariable Long id) {
        appRoleService.deleteRoleById(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK,
                        LocalDateTime.now(),
                        getDeletionMessage("Role", id)
                ), HttpStatus.OK
        );
    }
}
