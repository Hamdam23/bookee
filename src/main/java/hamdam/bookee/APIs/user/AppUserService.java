package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.helpers.UserImageDTO;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.UpdatePasswordRequest;
import hamdam.bookee.APIs.user.helpers.SetRoleUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    void saveUser(RegistrationRequest request);

    Page<AppUserResponseDTO> getAllUsers(Pageable pageable);

    AppUserResponseDTO getUserById(Long id);

    AppUserResponseDTO updateUser(AppUserRequestDTO newUser, Long id);

    AppUserResponseDTO setImageToUser(Long id, UserImageDTO imageDTO);

    AppUserResponseDTO setRoleToUser(Long id, SetRoleUserRequest setRoleUserRequest);

    void deleteUser(Long id);

    boolean existsWithUsername(String username);

    AppUserEntity getUserByUsername(String username, boolean withPermissions);

    AppUserResponseDTO updatePassword(UpdatePasswordRequest passwordDTO, Long id);
}
