package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AppUserService {
    AppUser addUser(AppUserDTO user);
    AppUser getUserByUsername(String userName);
    List<AppUser> getAllUsers();
    AppUser updateUser(AppUser newUser, long id);
    void deleteUser(long id);
    void setImageToUser(long id, UserImageDTO imageDTO);
    AppUser setRoleToUser(long id, AppUserRoleDTO appUserRoleDTO);
    void generateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
