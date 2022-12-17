package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.user.helpers.SetImageUserRequest;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.PasswordUpdateRequest;
import hamdam.bookee.APIs.user.helpers.SetRoleUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    void saveUser(RegistrationRequest request);

    Page<AppUserResponseDTO> getAllUsers(Pageable pageable);

    AppUserResponseDTO getUserById(Long id);

    AppUserResponseDTO updateUser(AppUserRequestDTO newUser, Long id);

    AppUserResponseDTO setImageToUser(Long id, SetImageUserRequest imageDTO);

    AppUserResponseDTO setRoleToUser(Long id, SetRoleUserRequest setRoleUserRequest);

    void deleteUser(Long id);

    boolean existsWithUsername(String username);

    AppUserEntity getUserByUsername(String username, boolean withPermissions);

    AppUserResponseDTO updatePassword(PasswordUpdateRequest passwordDTO, Long id);
}
