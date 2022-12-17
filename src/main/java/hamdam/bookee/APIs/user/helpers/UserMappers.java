package hamdam.bookee.APIs.user.helpers;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.helpers.ImageMappers;
import hamdam.bookee.APIs.role.helpers.RoleMappers;
import hamdam.bookee.APIs.user.AppUserEntity;
import org.springframework.beans.BeanUtils;

/**
 * It's a class that contains a bunch of methods that map a User object to a UserDTO object and vice
 * versa
 */
public class UserMappers {

    public static AppUserResponseDTO mapToAppUserResponseDTO(AppUserEntity entity) {
        if (entity == null) return null;
        AppUserResponseDTO response = new AppUserResponseDTO();
        BeanUtils.copyProperties(entity, response);
        if (entity.getRole() != null)
            response.setRole(RoleMappers.mapToAppRoleResponseDTO(entity.getRole()));
        if (entity.getUserImage() != null)
            response.setImage(ImageMappers.mapToImageDTO(entity.getUserImage()));
        return response;
    }

    public static AppUserEntity mapToAppUserEntity(RegistrationRequest dto) {
        if (dto == null) return null;
        AppUserEntity user = new AppUserEntity();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        return user;
    }
}
