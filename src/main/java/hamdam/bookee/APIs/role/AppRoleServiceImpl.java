package hamdam.bookee.APIs.role;

import hamdam.bookee.tools.exeptions.role.DuplicateRoleNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppRoleServiceImpl implements AppRoleService {
    private final AppRoleRepository appRoleRepository;

    @Override
    public AppRole addRole(AppRoleDTO appRole) {
        // TODO: 9/2/22 check if role name is unique
        if (isDuplicateRoleName(appRole.getRoleName())) {
            throw new DuplicateRoleNameException("Duplicate role name detected!");
        }
        return appRoleRepository.save(new AppRole(appRole));
    }

    @Override
    public List<AppRole> getAllRoles() {
        return appRoleRepository.findAll();
    }

    @Override
    public void deleteRoleById(long id) {
        appRoleRepository.deleteById(id);
    }

    private boolean isDuplicateRoleName(String roleName) {
        List<AppRole> roles = appRoleRepository.findAll();
        for(AppRole role: roles) {
            if (role.getRoleName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
