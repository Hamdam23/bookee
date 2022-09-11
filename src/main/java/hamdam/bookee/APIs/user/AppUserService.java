package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;

import java.util.List;

public interface AppUserService {
    AppUserEntity getUserByUsername(String userName);

    List<AppUserEntity> getAllUsers();

    // TODO: 9/2/22 don't use entity as update request argument
    //  fvck u
    AppUserEntity updateUser(AppUserDTO newUser, long id);

    void deleteUser(long id);

    // TODO: 9/2/22 why void, must return updated AppUser object
    void setImageToUser(long id, UserImageDTO imageDTO);

    AppUserEntity setRoleToUser(long id, AppUserRoleDTO appUserRoleDTO);

    // TODO: 9/2/22 rename (!)
    boolean isPasswordInvalid(String username);
}
