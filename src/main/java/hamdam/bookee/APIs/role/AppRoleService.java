package hamdam.bookee.APIs.role;

import java.util.List;

public interface AppRoleService {
    AppRole addRole(AppRoleDTO appRole);
    List<AppRole> getAllRoles();
    void deleteRoleById(long id);
}
