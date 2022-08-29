package hamdam.bookee.APIs.roleRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static hamdam.bookee.tools.constants.Endpoints.API_ROLE_REQUEST;

@RestController
@RequestMapping(API_ROLE_REQUEST)
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RoleRequestResponse> sendRoleRequest(@RequestBody RequestRole requestRole, HttpServletRequest request){
        return ResponseEntity.ok().body(requestService.postRoleRequest(requestRole, request));
    }
}
