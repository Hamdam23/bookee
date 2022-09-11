package hamdam.bookee.APIs.role;

import java.util.List;

public interface AppRoleService {
    AppRoleEntity addRole(AppRoleDTO appRole);
    List<AppRoleEntity> getAllRoles();
    void deleteRoleById(long id);
}
