package hamdam.bookee.APIs.role;

import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.DuplicateResourceException;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static hamdam.bookee.tools.token.GetUserByToken.getUserByRequest;

@Service
@RequiredArgsConstructor
public class AppRoleServiceImpl implements AppRoleService {

    private final AppRoleRepository roleRepository;
    private final AppUserRepository userRepository;

    @Override
    public AppRoleResponseDTO addRole(AppRoleRequestDTO appRole) {

        if (roleRepository.existsByRoleName(appRole.getRoleName())) {
            throw new DuplicateResourceException("role name");
        }
        AppRoleEntity roleEntity = new AppRoleEntity(appRole);

        return new AppRoleResponseDTO(roleRepository.save(roleEntity));
    }

    @Override
    public Page<AppRoleResponseDTO> getAllRoles(Pageable pageable) {
        return roleRepository.findAllByOrderByTimeStampDesc(pageable)
                .map(AppRoleResponseDTO::new);
    }

    @Override
    public ApiResponse deleteRoleById(Long id) {

        AppUserEntity currentUser = getUserByRequest(userRepository);

        if (currentUser.getRole().getPermissions().contains(MONITOR_ROLE)) {
            if (!roleRepository.existsById(id)) throw new ResourceNotFoundException("Role", "id", id);
            roleRepository.deleteById(id);

            return new ApiResponse(
                    HttpStatus.NO_CONTENT,
                    LocalDateTime.now(),
                    "Role with id: " + id + " successfully deleted!"
            );
        } else {
            throw new LimitedPermissionException();
        }
    }
}
