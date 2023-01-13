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

    public static UserResponseDTO mapToAppUserResponseDTO(AppUserEntity entity) {
        if (entity == null) return null;
        UserResponseDTO response = new UserResponseDTO();
        BeanUtils.copyProperties(entity, response);
        response.setRole(RoleMappers.mapToAppRoleResponseDTO(entity.getRole()));
        response.setImage(ImageMappers.mapToImageDTO(entity.getUserImage()));
        return response;
    }

    public static AppUserEntity mapToAppUserEntity(RegistrationRequest dto) {
        if (dto == null) return null;
        AppUserEntity user = new AppUserEntity();
        BeanUtils.copyProperties(dto, user, "password");
        return user;
    }
}
