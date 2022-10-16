package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.SetUserPasswordDTO;
import hamdam.bookee.APIs.user.helpers.SetUserRoleDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    void saveUser(RegistrationRequest request);

    Page<AppUserResponseDTO> getAllUsers(Pageable pageable);

    AppUserResponseDTO getUserById(Long id);

    // TODO: 9/2/22 don't use entity as update request argument
    //  fvck u
    AppUserResponseDTO updateUser(AppUserRequestDTO newUser, Long id);

    AppUserResponseDTO setImageToUser(Long id, UserImageDTO imageDTO);

    AppUserResponseDTO setRoleToUser(Long id, SetUserRoleDTO setUserRoleDTO);

    ApiResponse deleteUser(Long id);

    // TODO: 9/2/22 rename (!)
    boolean existsWithUsername(String username);

    AppUserEntity getUserByUsername(String username);

    AppUserResponseDTO updatePassword(SetUserPasswordDTO passwordDTO, Long id);
}
