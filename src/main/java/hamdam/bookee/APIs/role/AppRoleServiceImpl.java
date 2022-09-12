package hamdam.bookee.APIs.role;

import hamdam.bookee.tools.exceptions.role.DuplicateRoleNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppRoleServiceImpl implements AppRoleService {
    private final AppRoleRepository appRoleRepository;

    @Override
    public AppRoleEntity addRole(AppRoleDTO appRole) {
        // TODO: 9/2/22 check if role name is unique
        if (isDuplicateRoleName(appRole.getRoleName())) {
            throw new DuplicateRoleNameException("Duplicate role name detected!");
        }
        return appRoleRepository.save(new AppRoleEntity(appRole));
    }

    @Override
    public List<AppRoleEntity> getAllRoles() {
        return appRoleRepository.findAll();
    }

    @Override
    public void deleteRoleById(long id) {
        appRoleRepository.deleteById(id);
    }

    private boolean isDuplicateRoleName(String roleName) {
        List<AppRoleEntity> roles = appRoleRepository.findAll();
        for(AppRoleEntity role: roles) {
            if (role.getRoleName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
