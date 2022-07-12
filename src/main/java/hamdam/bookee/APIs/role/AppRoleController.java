package hamdam.bookee.APIs.role;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_ROLE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_ROLE)
public class AppRoleController {
    private final AppRoleService appRoleService;

    @PostMapping
    public AppRole postRole(@RequestBody AppRole appRole){
        return appRoleService.addRole(appRole);
    }

    @GetMapping
    public List<AppRole> getAllRoles(){
        return appRoleService.getAllRoles();
    }

    @DeleteMapping("{id}")
    public void deleteRoleById(@PathVariable long id){
        appRoleService.deleteRoleById(id);
    }
}
