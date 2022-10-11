package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role_request.helpers.ReviewStateDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    // TODO: 9/2/22 why ReviewState, for filtering purposes it is better to use request params
    @GetMapping
    public List<RoleRequestResponse> getAllRoleRequests(@RequestParam(required = false) State state) {
        return requestService.getAllRoleRequests(state);
    }

    @PutMapping("{id}")
    public ResponseEntity<RoleRequestResponse> reviewRequest(@PathVariable Long id,
                                                             @Valid @RequestBody ReviewStateDTO review) {
        return ResponseEntity.ok().body(requestService.reviewRequest(id, review));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long id){
        requestService.deleteRequest(id);
        // TODO: 9/2/22 return full json response, not plain text
        return ResponseEntity.ok().body("Role Request with id: [" + id + "] is successfully deleted.");
    }
}
