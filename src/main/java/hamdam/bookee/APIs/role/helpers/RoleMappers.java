package hamdam.bookee.APIs.role.helpers;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.Permissions;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * It contains static methods that map between the AppRoleEntity and AppRoleResponseDTO classes
 */
public class RoleMappers {

    public static AppRoleResponseDTO mapToAppRoleResponseDTO(AppRoleEntity entity) {
        AppRoleResponseDTO response = new AppRoleResponseDTO();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public static AppRoleEntity mapToAppRoleEntity(String roleName) {
        AppRoleEntity entity = new AppRoleEntity();
        entity.setRoleName(roleName);
        return entity;
    }

    public static AppRoleEntity mapToAppRoleEntity(String roleName, Set<Permissions> permissions) {
        AppRoleEntity entity = new AppRoleEntity();
        entity.setRoleName(roleName);
        entity.setPermissions(permissions);
        return entity;
    }

    public static AppRoleEntity mapToAppRoleEntity(String roleName, boolean isDefault, LocalDateTime timeStamp) {
        AppRoleEntity entity = new AppRoleEntity();
        entity.setRoleName(roleName);
        entity.setDefault(isDefault);
        entity.setTimeStamp(timeStamp);
        return entity;
    }

    public static AppRoleEntity mapToAppRoleEntity(AppRoleRequestDTO dto) {
        AppRoleEntity entity = new AppRoleEntity();
        entity.setRoleName(dto.getRoleName());
        entity.setPermissions(dto.getPermissions());
        entity.setDefault(dto.isDefault());
        return entity;
    }
}
