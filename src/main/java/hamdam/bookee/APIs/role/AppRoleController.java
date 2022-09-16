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

import javax.servlet.http.HttpServletRequest;

import static hamdam.bookee.tools.constants.Endpoints.API_ROLE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_ROLE)
public class AppRoleController {

    private final AppRoleService appRoleService;

    @PostMapping
    public AppRoleResponseDTO postRole(@RequestBody AppRoleRequestDTO appRole) {
        return appRoleService.addRole(appRole);
    }

    @GetMapping
    public PagedResponse<AppRoleResponseDTO> getAllRoles(@PageableDefault Pageable pageable) {
        return new PagedResponse<>(appRoleService.getAllRoles(pageable));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteRoleById(@PathVariable Long id) {
        return new ResponseEntity<>(appRoleService.deleteRoleById(id), HttpStatus.NO_CONTENT);
    }
}
