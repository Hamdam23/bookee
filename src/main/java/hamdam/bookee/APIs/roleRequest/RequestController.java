package hamdam.bookee.APIs.roleRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_ROLE_REQUEST;

@RestController
@RequestMapping(API_ROLE_REQUEST)
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RoleRequestResponse> sendRoleRequest(@RequestBody RequestRole requestRole, HttpServletRequest request) {
        return ResponseEntity.ok().body(requestService.postRoleRequest(requestRole, request));
    }

    @GetMapping
    public List<RoleRequestResponse> getAllRoleRequests(@RequestBody ReviewState reviewState,
                                                        @RequestBody Long userId,
                                                        HttpServletRequest request) {
        return requestService.getAllRoleRequests(reviewState, userId, request);
    }

    @PutMapping("{id}")
    public ResponseEntity<RoleRequestResponse> reviewRequest(@PathVariable Long id, @RequestBody ReviewState reviewState, HttpServletRequest request) {
        return ResponseEntity.ok().body(requestService.reviewRequest(id, reviewState, request));
    }
}
