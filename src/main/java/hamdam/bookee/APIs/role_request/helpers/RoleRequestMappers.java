package hamdam.bookee.APIs.role_request.helpers;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role_request.RoleRequestEntity;
import hamdam.bookee.APIs.role_request.State;
import hamdam.bookee.APIs.user.AppUserEntity;

public class RoleRequestMappers {

    public static ReviewRequestDTO mapToReviewRequestDTO(State state) {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setState(state);
        return request;
    }

    public static RoleRequestEntity mapToRoleRequestEntity(AppUserEntity user, AppRoleEntity requestedRole) {
        RoleRequestEntity entity = new RoleRequestEntity();
        entity.setUser(user);
        entity.setRequestedRole(requestedRole);
        return entity;
    }

    public static RoleRequestEntity mapToRoleRequestEntity(AppUserEntity user, AppRoleEntity requestedRole, State state) {
        RoleRequestEntity entity = new RoleRequestEntity();
        entity.setUser(user);
        entity.setRequestedRole(requestedRole);
        entity.setState(state);
        return entity;
    }
}
