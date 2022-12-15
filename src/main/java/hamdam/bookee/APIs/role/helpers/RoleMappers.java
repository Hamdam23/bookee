package hamdam.bookee.APIs.role.helpers;

import hamdam.bookee.APIs.role.AppRoleEntity;
import org.springframework.beans.BeanUtils;

/**
 * It contains static methods that map between the AppRoleEntity and AppRoleResponseDTO classes
 */
public class RoleMappers {

    public static AppRoleResponseDTO mapToAppRoleResponseDTO(AppRoleEntity entity) {
        if (entity == null) return null;
        AppRoleResponseDTO response = new AppRoleResponseDTO();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public static AppRoleEntity mapToAppRoleEntity(AppRoleRequestDTO dto) {
        if (dto == null) return null;
        AppRoleEntity entity = new AppRoleEntity();
        entity.setRoleName(dto.getRoleName());
        entity.setPermissions(dto.getPermissions());
        entity.setDefault(dto.isDefault());
        return entity;
    }
}
