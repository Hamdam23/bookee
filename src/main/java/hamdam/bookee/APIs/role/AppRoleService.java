package hamdam.bookee.APIs.role;

import java.util.List;

public interface AppRoleService {
    AppRole addRole(AppRole appRole);
    List<AppRole> getAllRoles();
    void deleteRoleById(long id);
}
