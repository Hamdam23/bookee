package hamdam.bookee.APIs.user.helpers;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.image.helpers.ImageResponseDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * It's a class that contains a bunch of methods that map a User object to a UserDTO object and vice
 * versa
 */
public class UserMappers {

    public static AppUserRequestDTO mapToAppUserRequestDTO(String name, String username) {
        AppUserRequestDTO request = new AppUserRequestDTO();
        request.setName(name);
        request.setUsername(username);
        return request;
    }

    public static AppUserRequestDTO mapToAppUserRequestDTO(String name, String username, Long roleId, Long imageId) {
        AppUserRequestDTO request = new AppUserRequestDTO();
        request.setName(name);
        request.setUsername(username);
        request.setRoleId(roleId);
        request.setImageId(imageId);
        return request;
    }

    public static AppRoleResponseDTO mapToAppUserRoleDTO(AppRoleEntity entity) {
        AppRoleResponseDTO userRole = new AppRoleResponseDTO();
        BeanUtils.copyProperties(entity, userRole);
        return userRole;
    }

    public static ImageResponseDTO mapToImageDTO(ImageEntity entity) {
        ImageResponseDTO image = new ImageResponseDTO();
        BeanUtils.copyProperties(entity, image);
        return image;
    }

    public static AppUserResponseDTO mapToAppUserResponseDTO(AppUserEntity entity) {
        if (entity == null) return null;
        AppUserResponseDTO response = new AppUserResponseDTO();
        BeanUtils.copyProperties(entity, response);
        if (entity.getRole() != null)
            response.setRole(mapToAppUserRoleDTO(entity.getRole()));
        if (entity.getUserImage() != null)
            response.setImage(mapToImageDTO(entity.getUserImage()));
        return response;
    }

    public static AppUserEntity mapToAppUserEntity(RegistrationRequest dto) {
        AppUserEntity user = new AppUserEntity();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        return user;
    }

    public static AppUserEntity mapToAppUserEntity(String username, AppRoleEntity role) {
        AppUserEntity user = new AppUserEntity();
        user.setUsername(username);
        user.setRole(role);
        return user;
    }

    public static AppUserEntity mapToAppUserEntity(String name, String username, String password) {
        AppUserEntity user = new AppUserEntity();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public static AppUserEntity mapToAppUserEntity(String name, String username, AppRoleEntity role) {
        AppUserEntity user = new AppUserEntity();
        user.setName(name);
        user.setUsername(username);
        user.setRole(role);
        return user;
    }

    public static AppUserEntity mapToAppUserEntity(String name, String username, String password, LocalDateTime timeStamp) {
        AppUserEntity user = new AppUserEntity();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setTimeStamp(timeStamp);
        return user;
    }

    public static AppUserEntity mapToAppUserEntity(String name, String username, String password, AppRoleEntity role) {
        AppUserEntity user = new AppUserEntity();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}
