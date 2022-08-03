package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;

import java.util.List;

public interface AppUserService {
    AppUser getUserByUsername(String userName);

    List<AppUser> getAllUsers();

    AppUser updateUser(AppUser newUser, long id);

    void deleteUser(long id);

    void setImageToUser(long id, UserImageDTO imageDTO);

    AppUser setRoleToUser(long id, AppUserRoleDTO appUserRoleDTO);

    boolean invalidPassword(String username);
}
