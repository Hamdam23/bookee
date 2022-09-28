package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.SetUserPasswordDTO;
import hamdam.bookee.APIs.user.helpers.SetUserRoleDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppUserService {
    Page<AppUserResponseDTO> getAllUsers(Pageable pageable);

    AppUserResponseDTO getUserById(Long id);

    // TODO: 9/2/22 don't use entity as update request argument
    //  fvck u
    AppUserResponseDTO updateUser(AppUserRequestDTO newUser, Long id);

    // TODO: 9/2/22 why void, must return updated AppUser object
    AppUserResponseDTO setImageToUser(Long id, UserImageDTO imageDTO);

    AppUserResponseDTO setRoleToUser(Long id, SetUserRoleDTO setUserRoleDTO);

    ApiResponse deleteUser(Long id);

    // TODO: 9/2/22 rename (!)
    boolean isPasswordInvalid(String username);

    AppUserEntity getUserByUsername(String username);

    AppUserResponseDTO updatePassword(SetUserPasswordDTO passwordDTO, Long id);
}
