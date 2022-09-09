package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;

import java.util.List;

public interface AppUserService {
    AppUser getUserByUsername(String userName);

    List<AppUser> getAllUsers();

    // TODO: 9/2/22 don't use entity as update request argument
    //  fvck u
    AppUser updateUser(AppUserDTO newUser, long id);

    void deleteUser(long id);

    // TODO: 9/2/22 why void, must return updated AppUser object
    void setImageToUser(long id, UserImageDTO imageDTO);

    AppUser setRoleToUser(long id, AppUserRoleDTO appUserRoleDTO);

    // TODO: 9/2/22 rename (!)
    boolean isPasswordInvalid(String username);
}
