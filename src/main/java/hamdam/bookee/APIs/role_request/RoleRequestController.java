package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role_request.helpers.ReviewRequestDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static hamdam.bookee.tools.constants.DeletionMessage.getDeletionMessage;
import static hamdam.bookee.tools.constants.Endpoints.API_ROLE_REQUEST;

@RestController
@RequestMapping(API_ROLE_REQUEST)
@RequiredArgsConstructor
public class RoleRequestController {

    private final RoleRequestService roleRequestService;

    /**
     * It takes a RoleRequestDTO object, validates it, and then sends it to the roleRequestService
     *
     * @param roleRequestDTO This is the object that will be sent to the server.
     * @return A RoleRequestResponse object
     */
    @PostMapping
    public RoleRequestResponse sendRoleRequest(@Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        return roleRequestService.postRoleRequest(roleRequestDTO);
    }

    /**
     * This function returns a list of all role requests, optionally filtered by state
     *
     * @param state The state of the role request.
     * @return List of RoleRequestResponse objects
     */
    @GetMapping
    public List<RoleRequestResponse> getAllRoleRequests(@RequestParam(required = false) State state) {
        return roleRequestService.getAllRoleRequests(state);
    }

    /**
     * > This function is used to review a request
     *
     * @param id The id of the role request
     * @param review This is the object that contains the review information.
     * @return A RoleRequestResponse object
     */
    @PutMapping("{id}")
    public RoleRequestResponse reviewRequest(@PathVariable Long id,
                                             @Valid @RequestBody ReviewRequestDTO review) {
        return roleRequestService.reviewRequest(id, review);
    }

    /**
     * It deletes a role request by id
     *
     * @param id The id of the role request to be deleted.
     * @return A response entity with a status code of 200 and a message that the role request has been
     * deleted.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteRequest(@PathVariable Long id) {
        roleRequestService.deleteRequest(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK,
                        LocalDateTime.now(),
                        getDeletionMessage("Role request", id)
                ), HttpStatus.OK
        );
    }
}
