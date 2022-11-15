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
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RoleRequestResponse> sendRoleRequest(@Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        return ResponseEntity.ok().body(requestService.postRoleRequest(roleRequestDTO));
    }

    @GetMapping
    public List<RoleRequestResponse> getAllRoleRequests(@RequestParam(required = false) State state) {
        return requestService.getAllRoleRequests(state);
    }

    @PutMapping("{id}")
    public ResponseEntity<RoleRequestResponse> reviewRequest(@PathVariable Long id,
                                                             @Valid @RequestBody ReviewRequestDTO review) {
        return ResponseEntity.ok().body(requestService.reviewRequest(id, review));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK,
                        LocalDateTime.now(),
                        getDeletionMessage("Role request", id)
                ), HttpStatus.OK
        );
    }
}
