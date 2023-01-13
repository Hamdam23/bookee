package hamdam.bookee.APIs.role_request.helpers;

import hamdam.bookee.APIs.role_request.RoleRequestEntity;
import hamdam.bookee.APIs.user.helpers.UserMappers;
import org.springframework.beans.BeanUtils;

/**
 * It contains methods that map between the DTOs and entities of the role request domain
 */
public class RoleRequestMappers {

    public static RoleRequestResponseDTO mapToRoleRequestResponse(RoleRequestEntity entity, String requestedRole) {
        if (entity == null) return null;
        RoleRequestResponseDTO response = new RoleRequestResponseDTO();
        response.setUser(UserMappers.mapToAppUserResponseDTO(entity.getUser()));
        response.setRequestedRole(requestedRole);
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
