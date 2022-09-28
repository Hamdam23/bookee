package hamdam.bookee.APIs.role;

import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppRoleService {

    AppRoleResponseDTO addRole(AppRoleRequestDTO appRole);

    Page<AppRoleResponseDTO> getAllRoles(Pageable pageable);

    ApiResponse deleteRoleById(Long id);
}
