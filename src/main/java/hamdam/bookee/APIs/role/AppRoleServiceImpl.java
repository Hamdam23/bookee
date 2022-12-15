package hamdam.bookee.APIs.role;

import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import hamdam.bookee.APIs.role.helpers.RoleMappers;
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
     * > If the role name already exists, throw a DuplicateResourceException, otherwise save the role
     * and return the response
     *
     * @param appRole The request object that contains the role name.
     * @return AppRoleResponseDTO
     */
    @Override
    public AppRoleResponseDTO addRole(AppRoleRequestDTO appRole) {

        if (roleRepository.existsByRoleName(appRole.getRoleName())) {
            throw new DuplicateResourceException("role name");
        }

        return RoleMappers.mapToAppRoleResponseDTO(roleRepository.save(RoleMappers.mapToAppRoleEntity(appRole)));
    }

    /**
     * It returns a page of AppRoleResponseDTO objects.
     *
     * @param pageable This is the pageable object that contains
     * the page number, page size, and sort order.
     * @return A Page of AppRoleResponseDTO
     */
    @Override
    public Page<AppRoleResponseDTO> getAllRoles(Pageable pageable) {
        return roleRepository.findAllByOrderByTimeStampDesc(pageable)
                .map(RoleMappers::mapToAppRoleResponseDTO);
    }

    /**
     * If the requesting user has the permission to monitor roles,
     * then delete the role with the given id
     *
     * @param id The id of the role to be deleted.
     */
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
