package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.user.helpers.SetUserImageRequest;
import hamdam.bookee.APIs.user.helpers.UserRequestDTO;
import hamdam.bookee.APIs.user.helpers.UserResponseDTO;
import hamdam.bookee.APIs.user.helpers.PasswordUpdateRequest;
import hamdam.bookee.APIs.user.helpers.SetUseRoleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    void saveUser(RegistrationRequest request);

    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(UserRequestDTO newUser, Long id);

    UserResponseDTO setImageToUser(Long id, SetUserImageRequest imageDTO);

    UserResponseDTO setRoleToUser(Long id, SetUseRoleRequest setUseRoleRequest);

    void deleteUser(Long id);

    boolean existsWithUsername(String username);

    AppUserEntity getUserByUsername(String username, boolean withPermissions);

    UserResponseDTO updatePassword(PasswordUpdateRequest passwordDTO, Long id);
}
