package hamdam.bookee.APIs.role;

import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.DuplicateResourceException;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE;
import static hamdam.bookee.tools.utils.SecurityUtils.getUserByRequest;

@Service
@RequiredArgsConstructor
public class AppRoleServiceImpl implements AppRoleService {

    private final AppRoleRepository roleRepository;
    private final AppUserRepository userRepository;

    /**
     * If the role name already exists, throw a DuplicateResourceException, otherwise save the role
     *
     * @param appRole The request object that contains the role name.
     * @return A new AppRoleResponseDTO object is being returned.
     */
    @Override
    public AppRoleResponseDTO addRole(AppRoleRequestDTO appRole) {

        if (roleRepository.existsByRoleName(appRole.getRoleName())) {
            throw new DuplicateResourceException("role name");
        }

        return new AppRoleResponseDTO(roleRepository.save(new AppRoleEntity(appRole)));
    }

    @Override
    public Page<AppRoleResponseDTO> getAllRoles(Pageable pageable) {
        return roleRepository.findAllByOrderByTimeStampDesc(pageable)
                .map(AppRoleResponseDTO::new);
    }

    @Override
    public void deleteRoleById(Long id) {

        AppUserEntity requestingUser = getUserByRequest(userRepository);

        if (requestingUser.getRole().getPermissions().contains(MONITOR_ROLE)) {
            if (!roleRepository.existsById(id)) throw new ResourceNotFoundException("Role", "id", id);
            roleRepository.deleteById(id);
        } else {
            throw new LimitedPermissionException();
        }
    }
}
